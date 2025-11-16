package io.github.artsobol.kurkod.web.controller.workshop;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.common.error.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.workshop.error.WorkshopError;
import io.github.artsobol.kurkod.web.domain.workshop.model.dto.WorkshopDTO;
import io.github.artsobol.kurkod.web.domain.workshop.model.request.WorkshopPatchRequest;
import io.github.artsobol.kurkod.web.domain.workshop.model.request.WorkshopPostRequest;
import io.github.artsobol.kurkod.web.domain.workshop.model.request.WorkshopPutRequest;
import io.github.artsobol.kurkod.web.domain.workshop.service.api.WorkshopService;
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

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = WorkshopController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class WorkshopControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/workshops";
    private static final int WORKSHOP_ID = 42;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean WorkshopService workshopService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    @Nested
    class GetById {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            WorkshopDTO dto = createDto(WORKSHOP_ID, 7, 3L);
            when(workshopService.get(WORKSHOP_ID)).thenReturn(dto);

            mvc.perform(get(API + "/{id}", WORKSHOP_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(3L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(WORKSHOP_ID))
               .andExpect(jsonPath("$.payload.workshopNumber").value(7))
               .andExpect(jsonPath("$.payload.version").value(3));

            verify(workshopService).get(WORKSHOP_ID);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.workshop.WorkshopControllerTest#locales")
        @DisplayName("404 Not Found: not found by id")
        void returns404(Locale locale) throws Exception {
            when(workshopService.get(WORKSHOP_ID))
                    .thenThrow(new NotFoundException(WorkshopError.NOT_FOUND_BY_ID, WORKSHOP_ID));

            mvc.perform(get(API + "/{id}", WORKSHOP_ID).accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Workshop with ID=%d not found",
                       "Цех с ID=%d не найден",
                       WORKSHOP_ID)));

            verify(workshopService).get(WORKSHOP_ID);
        }
    }

    @Nested
    class GetAll {
        @Test
        @DisplayName("200 OK: list")
        void returns200_with_list() throws Exception {
            List<WorkshopDTO> list = List.of(
                    createDto(1, 1, 1L),
                    createDto(2, 2, 1L)
                                            );
            when(workshopService.getAll()).thenReturn(list);

            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(2)))
               .andExpect(jsonPath("$.payload[0].workshopNumber").value(1))
               .andExpect(jsonPath("$.payload[1].workshopNumber").value(2));

            verify(workshopService).getAll();
        }

        @Test
        @DisplayName("200 OK: empty list")
        void returns200_with_empty_list() throws Exception {
            when(workshopService.getAll()).thenReturn(List.of());

            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(workshopService).getAll();
        }
    }

    @Nested
    class Create {
        @Test
        @DisplayName("201 Created: body + ETag + Location")
        void returns201_with_payload_etag_and_location() throws Exception {
            WorkshopPostRequest request = createPostRequest(9);
            WorkshopDTO dto = createDto(100, 9, 1L);
            when(workshopService.create(any(WorkshopPostRequest.class))).thenReturn(dto);

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(header().string("Location", endsWith(API + "/100")))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(100))
               .andExpect(jsonPath("$.payload.workshopNumber").value(9))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(workshopService).create(any(WorkshopPostRequest.class));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.workshop.WorkshopControllerTest#locales")
        @DisplayName("409 Conflict: workshop number already exists")
        void returns409_when_already_exists(Locale locale) throws Exception {
            WorkshopPostRequest request = createPostRequest(7);
            when(workshopService.create(any(WorkshopPostRequest.class)))
                    .thenThrow(new DataExistException(WorkshopError.ALREADY_EXISTS, 7));

            mvc.perform(post(API).accept(JSON).locale(locale).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isConflict())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Workshop with number=%d already exists",
                       "Цех с номером=%d уже существует",
                       7)));

            verify(workshopService).create(any(WorkshopPostRequest.class));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(workshopService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_body_invalid() throws Exception {
            WorkshopPostRequest bad = createPostRequest(-1);

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(workshopService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON).content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(workshopService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(MediaType.TEXT_PLAIN).content("workshopNumber=9"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(workshopService);
        }
    }

    @Nested
    class Replace {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long current = 5L, next = 6L;
            WorkshopPutRequest request = createPutRequest(10);
            WorkshopDTO dto = createDto(WORKSHOP_ID, 10, next);
            when(workshopService.replace(eq(WORKSHOP_ID), any(WorkshopPutRequest.class), eq(current)))
                    .thenReturn(dto);

            mvc.perform(put(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                       .contentType(JSON)
                                                       .header("If-Match", convertToEtag(current))
                                                       .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(next)))
               .andExpect(jsonPath("$.payload.version").value((int) next));

            verify(workshopService).replace(eq(WORKSHOP_ID), any(WorkshopPutRequest.class), eq(current));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.workshop.WorkshopControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            WorkshopPutRequest request = createPutRequest(11);

            mvc.perform(put(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                       .locale(locale)
                                                       .contentType(JSON)
                                                       .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            WorkshopPutRequest request = createPutRequest(12);

            mvc.perform(put(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                       .contentType(JSON)
                                                       .header("If-Match", "trash")
                                                       .content(convertToJson(request)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(put(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                       .contentType(JSON)
                                                       .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(workshopService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            WorkshopPutRequest bad = createPutRequest(-10);

            mvc.perform(put(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                       .contentType(JSON)
                                                       .header("If-Match", convertToEtag(5L))
                                                       .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(workshopService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.workshop.WorkshopControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(workshopService.replace(eq(WORKSHOP_ID), any(WorkshopPutRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            WorkshopPutRequest request = createPutRequest(13);
            mvc.perform(put(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                       .locale(locale)
                                                       .contentType(JSON)
                                                       .header("If-Match", convertToEtag(current))
                                                       .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(workshopService).replace(eq(WORKSHOP_ID), any(WorkshopPutRequest.class), eq(current));
        }
    }

    @Nested
    class Update {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long current = 7L, next = 8L;
            WorkshopPatchRequest request = createPatchRequest(20);
            WorkshopDTO dto = createDto(WORKSHOP_ID, 20, next);
            when(workshopService.update(eq(WORKSHOP_ID), any(WorkshopPatchRequest.class), eq(current)))
                    .thenReturn(dto);

            mvc.perform(patch(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                         .contentType(JSON)
                                                         .header("If-Match", convertToEtag(current))
                                                         .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(next)))
               .andExpect(jsonPath("$.payload.version").value((int) next));

            verify(workshopService).update(eq(WORKSHOP_ID), any(WorkshopPatchRequest.class), eq(current));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                         .contentType(JSON)
                                                         .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(workshopService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            WorkshopPatchRequest request = createPatchRequest(21);

            mvc.perform(patch(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                         .contentType(JSON)
                                                         .header("If-Match", "bad")
                                                         .content(convertToJson(request)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            WorkshopPatchRequest bad = createPatchRequest(-1);

            mvc.perform(patch(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                         .contentType(JSON)
                                                         .header("If-Match", convertToEtag(5L))
                                                         .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(workshopService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.workshop.WorkshopControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            WorkshopPatchRequest request = createPatchRequest(22);

            mvc.perform(patch(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                         .locale(locale)
                                                         .contentType(JSON)
                                                         .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.workshop.WorkshopControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(workshopService.update(eq(WORKSHOP_ID), any(WorkshopPatchRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            WorkshopPatchRequest request = createPatchRequest(23);
            mvc.perform(patch(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                         .locale(locale)
                                                         .contentType(JSON)
                                                         .header("If-Match", convertToEtag(current))
                                                         .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(workshopService).update(eq(WORKSHOP_ID), any(WorkshopPatchRequest.class), eq(current));
        }
    }

    @Nested
    class DeleteOne {
        @Test
        @DisplayName("204 No Content")
        void returns204() throws Exception {
            long version = 5L;
            doNothing().when(workshopService).delete(WORKSHOP_ID, version);

            mvc.perform(delete(API + "/{id}", WORKSHOP_ID).accept(JSON).header("If-Match", convertToEtag(version)))
               .andExpect(status().isNoContent())
               .andExpect(header().doesNotExist("ETag"))
               .andExpect(content().string(""));

            verify(workshopService).delete(WORKSHOP_ID, version);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.workshop.WorkshopControllerTest#locales")
        @DisplayName("404 Not Found: not found by id")
        void returns404(Locale locale) throws Exception {
            long version = 5L;
            doThrow(new NotFoundException(WorkshopError.NOT_FOUND_BY_ID, WORKSHOP_ID))
                    .when(workshopService).delete(WORKSHOP_ID, version);

            mvc.perform(delete(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                          .locale(locale)
                                                          .header("If-Match", convertToEtag(version)))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Workshop with ID=%s not found",
                       "Цех с ID=%s не найден",
                       WORKSHOP_ID)));

            verify(workshopService).delete(WORKSHOP_ID, version);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.workshop.WorkshopControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            mvc.perform(delete(API + "/{id}", WORKSHOP_ID).accept(JSON).locale(locale))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_wrong_etag() throws Exception {
            mvc.perform(delete(API + "/{id}", WORKSHOP_ID).accept(JSON).header("If-Match", "bad"))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.workshop.WorkshopControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected))
                    .when(workshopService).delete(WORKSHOP_ID, current);

            mvc.perform(delete(API + "/{id}", WORKSHOP_ID).accept(JSON)
                                                          .locale(locale)
                                                          .header("If-Match", convertToEtag(current)))
               .andExpect(status().isPreconditionFailed());

            verify(workshopService).delete(WORKSHOP_ID, current);
        }
    }

    private WorkshopDTO createDto(Integer id, Integer workshopNumber, Long version) {
        return new WorkshopDTO(id, workshopNumber, version);
    }

    private WorkshopPostRequest createPostRequest(Integer workshopNumber) {
        WorkshopPostRequest r = new WorkshopPostRequest();
        r.setWorkshopNumber(workshopNumber);
        return r;
    }

    private WorkshopPutRequest createPutRequest(Integer workshopNumber) {
        WorkshopPutRequest r = new WorkshopPutRequest();
        r.setWorkshopNumber(workshopNumber);
        return r;
    }

    private WorkshopPatchRequest createPatchRequest(Integer workshopNumber) {
        WorkshopPatchRequest r = new WorkshopPatchRequest();
        r.setWorkshopNumber(workshopNumber);
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
