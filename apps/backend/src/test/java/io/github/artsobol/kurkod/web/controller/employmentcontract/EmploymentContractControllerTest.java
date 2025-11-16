package io.github.artsobol.kurkod.web.controller.employmentcontract;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.common.error.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.dto.EmploymentContractDTO;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.request.EmploymentContractPatchRequest;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.request.EmploymentContractPostRequest;
import io.github.artsobol.kurkod.web.domain.employmentcontract.model.request.EmploymentContractPutRequest;
import io.github.artsobol.kurkod.web.domain.employmentcontract.service.api.EmploymentContractService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EmploymentContractController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class EmploymentContractControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/workers/{workerId}/contract";
    private static final int WORKER_ID = 12;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean EmploymentContractService employmentContractService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    // ---------- GET ----------
    @Nested
    class GetByWorkerId {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            EmploymentContractDTO dto = createDto("CN-1", 1000, "Dev", "John", "Doe",
                                                  LocalDate.now(), LocalDate.now().plusDays(10), 1L);
            when(employmentContractService.get(WORKER_ID)).thenReturn(dto);

            mvc.perform(get(API, WORKER_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.contractNumber").value("CN-1"))
               .andExpect(jsonPath("$.payload.salary").value(1000))
               .andExpect(jsonPath("$.payload.position").value("Dev"))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(employmentContractService).get(WORKER_ID);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.employmentcontract.EmploymentContractControllerTest#locales")
        @DisplayName("404 Not Found: no contract for worker")
        void returns404_with_i18n_message(Locale locale) throws Exception {
            when(employmentContractService.get(WORKER_ID)).thenThrow(new NotFoundException(
                    io.github.artsobol.kurkod.web.domain.employmentcontract.error.EmploymentContractError.NOT_FOUND_BY_WORKER_ID,
                    WORKER_ID
            ));

            mvc.perform(get(API, WORKER_ID).accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Employment contract for worker ID=%d not found",
                       "Трудовой договор для работника с ID=%d не найден",
                       WORKER_ID)));

            verify(employmentContractService).get(WORKER_ID);
        }
    }

    // ---------- POST ----------
    @Nested
    class Create {
        @Test
        @DisplayName("201 Created: body + ETag + Location")
        void returns201_with_payload_etag_and_location() throws Exception {
            EmploymentContractPostRequest req = createPostRequest("CN-2", 2000, 7,
                                                                  LocalDate.now(), LocalDate.now().plusDays(30));
            EmploymentContractDTO dto = createDto("CN-2", 2000, "QA", "Ann", "Lee",
                                                  req.getStartDate(), req.getEndDate(), 3L);

            when(employmentContractService.create(eq(WORKER_ID), any(EmploymentContractPostRequest.class)))
                    .thenReturn(dto);

            mvc.perform(post(API, WORKER_ID).accept(JSON).contentType(JSON).content(convertToJson(req)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(3L)))
               .andExpect(header().string("Location", endsWith("/api/v1/workers/" + WORKER_ID + "/contract")))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.contractNumber").value("CN-2"))
               .andExpect(jsonPath("$.payload.version").value(3));

            verify(employmentContractService).create(eq(WORKER_ID), any(EmploymentContractPostRequest.class));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API, WORKER_ID).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(employmentContractService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_body_invalid() throws Exception {
            EmploymentContractPostRequest bad = new EmploymentContractPostRequest(); // поля пустые
            mvc.perform(post(API, WORKER_ID).accept(JSON).contentType(JSON).content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(employmentContractService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API, WORKER_ID).accept(JSON).contentType(JSON).content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(employmentContractService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API, WORKER_ID).accept(JSON)
                                            .contentType(MediaType.TEXT_PLAIN)
                                            .content("contractNumber=CN&salary=1000"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(employmentContractService);
        }
    }

    @Nested
    class Replace {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long oldV = 5L, newV = 6L;
            EmploymentContractPutRequest req = createPutRequest("CN-X", 3000, 3,
                                                                LocalDate.now(), LocalDate.now().plusDays(5));
            EmploymentContractDTO dto = createDto("CN-X", 3000, "Lead", "Max", "Fox",
                                                  req.getStartDate(), req.getEndDate(), newV);

            when(employmentContractService.replace(eq(WORKER_ID), any(EmploymentContractPutRequest.class), eq(oldV)))
                    .thenReturn(dto);

            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .contentType(JSON)
                                           .header("If-Match", convertToEtag(oldV))
                                           .content(convertToJson(req)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(newV)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.version").value((int) newV));

            verify(employmentContractService).replace(eq(WORKER_ID), any(EmploymentContractPutRequest.class), eq(oldV));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.employmentcontract.EmploymentContractControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            EmploymentContractPutRequest req = createPutRequest("CN", 1, 1,
                                                                LocalDate.now(), LocalDate.now().plusDays(1));

            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .locale(locale)
                                           .contentType(JSON)
                                           .content(convertToJson(req)))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.employmentcontract.EmploymentContractControllerTest#locales")
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_invalid_etag(Locale locale) throws Exception {
            EmploymentContractPutRequest req = createPutRequest("CN", 1, 1,
                                                                LocalDate.now(), LocalDate.now().plusDays(1));

            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .locale(locale)
                                           .contentType(JSON)
                                           .header("If-Match", "trash")
                                           .content(convertToJson(req)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .contentType(JSON)
                                           .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(employmentContractService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            EmploymentContractPutRequest bad = new EmploymentContractPutRequest(); // пусто
            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .contentType(JSON)
                                           .header("If-Match", convertToEtag(5L))
                                           .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(employmentContractService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .contentType(JSON)
                                           .header("If-Match", convertToEtag(5L))
                                           .content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(employmentContractService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .contentType(MediaType.TEXT_PLAIN)
                                           .header("If-Match", convertToEtag(5L))
                                           .content("contractNumber=CN"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(employmentContractService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.employmentcontract.EmploymentContractControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(employmentContractService.replace(eq(WORKER_ID), any(EmploymentContractPutRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            EmploymentContractPutRequest req = createPutRequest("CN-Z", 1, 1,
                                                                LocalDate.now(), LocalDate.now().plusDays(1));
            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .locale(locale)
                                           .contentType(JSON)
                                           .header("If-Match", convertToEtag(current))
                                           .content(convertToJson(req)))
               .andExpect(status().isPreconditionFailed());

            verify(employmentContractService).replace(eq(WORKER_ID), any(EmploymentContractPutRequest.class), eq(current));
        }
    }

    // ---------- PATCH ----------
    @Nested
    class Update {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long oldV = 5L, newV = 6L;
            EmploymentContractPatchRequest req = createPatchRequest("CN-3", 2500, 9, null, null);
            EmploymentContractDTO dto = createDto("CN-3", 2500, "PM", "Kate", "Sim",
                                                  LocalDate.now(), LocalDate.now().plusDays(2), newV);
            when(employmentContractService.update(eq(WORKER_ID), any(EmploymentContractPatchRequest.class), eq(oldV)))
                    .thenReturn(dto);

            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .contentType(JSON)
                                             .header("If-Match", convertToEtag(oldV))
                                             .content(convertToJson(req)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(newV)))
               .andExpect(jsonPath("$.payload.version").value((int) newV));

            verify(employmentContractService).update(eq(WORKER_ID), any(EmploymentContractPatchRequest.class), eq(oldV));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .contentType(JSON)
                                             .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(employmentContractService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            // нарушаем размер и знак
            EmploymentContractPatchRequest bad = createPatchRequest("X", -1, null,
                                                                    LocalDate.now(), LocalDate.now().minusDays(1));
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .contentType(JSON)
                                             .header("If-Match", convertToEtag(5L))
                                             .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(employmentContractService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception{
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .contentType(JSON)
                                             .header("If-Match", convertToEtag(5L))
                                             .content("["))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(employmentContractService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .contentType(MediaType.TEXT_PLAIN)
                                             .header("If-Match", convertToEtag(5L))
                                             .content("contractNumber=CN-3"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(employmentContractService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.employmentcontract.EmploymentContractControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            EmploymentContractPatchRequest req = createPatchRequest("CN-3", 2500, 9, null, null);
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .locale(locale)
                                             .contentType(JSON)
                                             .content(convertToJson(req)))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.employmentcontract.EmploymentContractControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(employmentContractService.update(eq(WORKER_ID), any(EmploymentContractPatchRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            EmploymentContractPatchRequest req = createPatchRequest("CN-4", 1, null, null, null);
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .locale(locale)
                                             .contentType(JSON)
                                             .header("If-Match", convertToEtag(current))
                                             .content(convertToJson(req)))
               .andExpect(status().isPreconditionFailed());

            verify(employmentContractService).update(eq(WORKER_ID), any(EmploymentContractPatchRequest.class), eq(current));
        }
    }

    // ---------- DELETE ----------
    @Nested
    class Delete {
        @Test
        @DisplayName("204 No Content: no ETag and empty body")
        void returns204_without_etag() throws Exception {
            long v = 5L;
            doNothing().when(employmentContractService).delete(WORKER_ID, v);

            mvc.perform(delete(API, WORKER_ID).accept(JSON).header("If-Match", convertToEtag(v)))
               .andExpect(status().isNoContent())
               .andExpect(header().doesNotExist("ETag"))
               .andExpect(content().string(""));

            verify(employmentContractService).delete(WORKER_ID, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.employmentcontract.EmploymentContractControllerTest#locales")
        @DisplayName("404 Not Found: not found")
        void returns404_when_not_found(Locale locale) throws Exception {
            long v = 5L;
            doThrow(new NotFoundException(
                    io.github.artsobol.kurkod.web.domain.employmentcontract.error.EmploymentContractError.NOT_FOUND_BY_WORKER_ID,
                    WORKER_ID)).when(employmentContractService).delete(WORKER_ID, v);

            mvc.perform(delete(API, WORKER_ID).accept(JSON).locale(locale).header("If-Match", convertToEtag(v)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Employment contract for worker ID=%s not found",
                       "Трудовой договор для работника с ID=%s не найден",
                       WORKER_ID)));

            verify(employmentContractService).delete(WORKER_ID, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.employmentcontract.EmploymentContractControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            mvc.perform(delete(API, WORKER_ID).accept(JSON).locale(locale))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.employmentcontract.EmploymentContractControllerTest#locales")
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_wrong_etag(Locale locale) throws Exception {
            mvc.perform(delete(API, WORKER_ID).accept(JSON).locale(locale).header("If-Match", "bad"))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.employmentcontract.EmploymentContractControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected))
                    .when(employmentContractService).delete(WORKER_ID, current);

            mvc.perform(delete(API, WORKER_ID).accept(JSON)
                                              .locale(locale)
                                              .header("If-Match", convertToEtag(current)))
               .andExpect(status().isPreconditionFailed());

            verify(employmentContractService).delete(WORKER_ID, current);
        }
    }

    private EmploymentContractDTO createDto(String cn, Integer salary, String position,
                                            String firstName, String lastName,
                                            LocalDate start, LocalDate end,
                                            long version) {
        return new EmploymentContractDTO(
                cn, salary, position, firstName, lastName,
                start, end,
                OffsetDateTime.now(), OffsetDateTime.now(),
                version
        );
    }

    private EmploymentContractPostRequest createPostRequest(String cn, Integer salary, Integer staffId,
                                                            LocalDate start, LocalDate end) {
        EmploymentContractPostRequest r = new EmploymentContractPostRequest();
        r.setContractNumber(cn);
        r.setSalary(salary);
        r.setStaffId(staffId);
        r.setStartDate(start);
        r.setEndDate(end);
        return r;
    }

    private EmploymentContractPutRequest createPutRequest(String cn, Integer salary, Integer staffId,
                                                          LocalDate start, LocalDate end) {
        EmploymentContractPutRequest r = new EmploymentContractPutRequest();
        r.setContractNumber(cn);
        r.setSalary(salary);
        r.setStaffId(staffId);
        r.setStartDate(start);
        r.setEndDate(end);
        return r;
    }

    private EmploymentContractPatchRequest createPatchRequest(String cn, Integer salary, Integer staffId,
                                                              LocalDate start, LocalDate end) {
        EmploymentContractPatchRequest r = new EmploymentContractPatchRequest();
        r.setContractNumber(cn);
        r.setSalary(salary);
        r.setStaffId(staffId);
        r.setStartDate(start);
        r.setEndDate(end);
        return r;
    }

    private String convertToJson(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }

    private String convertToEtag(long version) {
        return EtagUtils.toEtag(version);
    }

    static Stream<Locale> locales() {
        return Stream.of(Locale.ENGLISH, Locale.of("ru", "RU"));
    }

    private static String createMessage(Locale locale, String en, String ru, Object... args) {
        String pattern = locale.getLanguage().equals("ru") ? ru : en;
        return args.length == 0 ? pattern : String.format(pattern, args);
    }
}
