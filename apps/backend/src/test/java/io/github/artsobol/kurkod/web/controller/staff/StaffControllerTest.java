package io.github.artsobol.kurkod.web.controller.staff;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.common.exception.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.staff.error.StaffError;
import io.github.artsobol.kurkod.web.domain.staff.model.dto.StaffDTO;
import io.github.artsobol.kurkod.web.domain.staff.model.request.StaffPatchRequest;
import io.github.artsobol.kurkod.web.domain.staff.model.request.StaffPostRequest;
import io.github.artsobol.kurkod.web.domain.staff.model.request.StaffPutRequest;
import io.github.artsobol.kurkod.web.domain.staff.service.api.StaffService;
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

import java.time.OffsetDateTime;
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

@WebMvcTest(controllers = StaffController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class StaffControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/staff";
    private static final int STAFF_ID = 7;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean StaffService staffService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    @Nested
    class GetById {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            StaffDTO dto = createDto(STAFF_ID, "Supervisor", 1L);
            when(staffService.get(STAFF_ID)).thenReturn(dto);

            mvc.perform(get(API + "/{id}", STAFF_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(STAFF_ID))
               .andExpect(jsonPath("$.payload.position").value("Supervisor"))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(staffService).get(STAFF_ID);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.staff.StaffControllerTest#locales")
        @DisplayName("404 Not Found: not found by id")
        void returns404(Locale locale) throws Exception {
            when(staffService.get(STAFF_ID))
                    .thenThrow(new NotFoundException(StaffError.NOT_FOUND_BY_ID, STAFF_ID));

            mvc.perform(get(API + "/{id}", STAFF_ID).accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Staff with ID=%d not found",
                       "Должность с ID=%d не найдена",
                       STAFF_ID)));

            verify(staffService).get(STAFF_ID);
        }
    }

    @Nested
    class GetAll {
        @Test
        @DisplayName("200 OK: list")
        void returns200_with_list() throws Exception {
            List<StaffDTO> list = List.of(
                    createDto(1, "Worker", 1L),
                    createDto(2, "Manager", 1L)
                                         );
            when(staffService.getAll()).thenReturn(list);

            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(2)))
               .andExpect(jsonPath("$.payload[0].position").value("Worker"))
               .andExpect(jsonPath("$.payload[1].position").value("Manager"));

            verify(staffService).getAll();
        }

        @Test
        @DisplayName("200 OK: empty list")
        void returns200_with_empty_list() throws Exception {
            when(staffService.getAll()).thenReturn(List.of());

            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(staffService).getAll();
        }
    }

    @Nested
    class Create {
        @Test
        @DisplayName("201 Created: body + ETag + Location")
        void returns201_with_payload_etag_and_location() throws Exception {
            StaffPostRequest request = createPostRequest("Supervisor");
            StaffDTO dto = createDto(101, "Supervisor", 1L);
            when(staffService.create(any(StaffPostRequest.class))).thenReturn(dto);

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(header().string("Location", endsWith(API + "/101")))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(101))
               .andExpect(jsonPath("$.payload.position").value("Supervisor"))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(staffService).create(any(StaffPostRequest.class));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.staff.StaffControllerTest#locales")
        @DisplayName("409 Conflict: position already exists")
        void returns409_when_already_exists(Locale locale) throws Exception {
            StaffPostRequest request = createPostRequest("Supervisor");
            when(staffService.create(any(StaffPostRequest.class)))
                    .thenThrow(new DataExistException(StaffError.ALREADY_EXISTS, "Supervisor"));

            mvc.perform(post(API).accept(JSON).locale(locale).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isConflict())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Staff position %s already exists",
                       "Должность %s уже существует",
                       "Supervisor")));

            verify(staffService).create(any(StaffPostRequest.class));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(staffService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_body_invalid() throws Exception {
            StaffPostRequest bad = createPostRequest("A");

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(staffService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON).content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(staffService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API).accept(JSON)
                                 .contentType(MediaType.TEXT_PLAIN)
                                 .content("position=Supervisor"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(staffService);
        }
    }

    @Nested
    class Replace {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long current = 5L, next = 6L;
            StaffPutRequest request = createPutRequest("Senior Supervisor");
            StaffDTO dto = createDto(STAFF_ID, "Senior Supervisor", next);
            when(staffService.replace(eq(STAFF_ID), any(StaffPutRequest.class), eq(current))).thenReturn(dto);

            mvc.perform(put(API + "/{id}", STAFF_ID).accept(JSON)
                                                    .contentType(JSON)
                                                    .header("If-Match", convertToEtag(current))
                                                    .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(next)))
               .andExpect(jsonPath("$.payload.version").value((int) next));

            verify(staffService).replace(eq(STAFF_ID), any(StaffPutRequest.class), eq(current));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.staff.StaffControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            StaffPutRequest request = createPutRequest("SuperVisor");

            mvc.perform(put(API + "/{id}", STAFF_ID).accept(JSON)
                                                    .locale(locale)
                                                    .contentType(JSON)
                                                    .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            StaffPutRequest request = createPutRequest("X");

            mvc.perform(put(API + "/{id}", STAFF_ID).accept(JSON)
                                                    .contentType(JSON)
                                                    .header("If-Match", "trash")
                                                    .content(convertToJson(request)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(put(API + "/{id}", STAFF_ID).accept(JSON)
                                                    .contentType(JSON)
                                                    .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(staffService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            StaffPutRequest bad = createPutRequest("A");

            mvc.perform(put(API + "/{id}", STAFF_ID).accept(JSON)
                                                    .contentType(JSON)
                                                    .header("If-Match", convertToEtag(5L))
                                                    .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(staffService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.staff.StaffControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(staffService.replace(eq(STAFF_ID), any(StaffPutRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            StaffPutRequest request = createPutRequest("SuperVisor");
            mvc.perform(put(API + "/{id}", STAFF_ID).accept(JSON)
                                                    .locale(locale)
                                                    .contentType(JSON)
                                                    .header("If-Match", convertToEtag(current))
                                                    .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(staffService).replace(eq(STAFF_ID), any(StaffPutRequest.class), eq(current));
        }
    }

    @Nested
    class Update {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long current = 2L, next = 3L;
            StaffPatchRequest request = createPatchRequest("Lead");
            StaffDTO dto = createDto(STAFF_ID, "Lead", next);
            when(staffService.update(eq(STAFF_ID), any(StaffPatchRequest.class), eq(current))).thenReturn(dto);

            mvc.perform(patch(API + "/{id}", STAFF_ID).accept(JSON)
                                                      .contentType(JSON)
                                                      .header("If-Match", convertToEtag(current))
                                                      .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(next)))
               .andExpect(jsonPath("$.payload.version").value((int) next));

            verify(staffService).update(eq(STAFF_ID), any(StaffPatchRequest.class), eq(current));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API + "/{id}", STAFF_ID).accept(JSON)
                                                      .contentType(JSON)
                                                      .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(staffService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            StaffPatchRequest request = createPatchRequest("Lead");

            mvc.perform(patch(API + "/{id}", STAFF_ID).accept(JSON)
                                                      .contentType(JSON)
                                                      .header("If-Match", "bad")
                                                      .content(convertToJson(request)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            StaffPatchRequest bad = createPatchRequest("A");

            mvc.perform(patch(API + "/{id}", STAFF_ID).accept(JSON)
                                                      .contentType(JSON)
                                                      .header("If-Match", convertToEtag(5L))
                                                      .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(staffService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.staff.StaffControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            StaffPatchRequest request = createPatchRequest("Lead");

            mvc.perform(patch(API + "/{id}", STAFF_ID).accept(JSON)
                                                      .locale(locale)
                                                      .contentType(JSON)
                                                      .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.staff.StaffControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(staffService.update(eq(STAFF_ID), any(StaffPatchRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            StaffPatchRequest request = createPatchRequest("Lead");
            mvc.perform(patch(API + "/{id}", STAFF_ID).accept(JSON)
                                                      .locale(locale)
                                                      .contentType(JSON)
                                                      .header("If-Match", convertToEtag(current))
                                                      .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(staffService).update(eq(STAFF_ID), any(StaffPatchRequest.class), eq(current));
        }
    }

    @Nested
    class DeleteOne {
        @Test
        @DisplayName("204 No Content")
        void returns204() throws Exception {
            long version = 5L;
            doNothing().when(staffService).delete(STAFF_ID, version);

            mvc.perform(delete(API + "/{id}", STAFF_ID).accept(JSON).header("If-Match", convertToEtag(version)))
               .andExpect(status().isNoContent())
               .andExpect(header().doesNotExist("ETag"))
               .andExpect(content().string(""));

            verify(staffService).delete(STAFF_ID, version);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.staff.StaffControllerTest#locales")
        @DisplayName("404 Not Found")
        void returns404(Locale locale) throws Exception {
            long version = 5L;
            doThrow(new NotFoundException(StaffError.NOT_FOUND_BY_ID, STAFF_ID))
                    .when(staffService).delete(STAFF_ID, version);

            mvc.perform(delete(API + "/{id}", STAFF_ID).accept(JSON)
                                                       .locale(locale)
                                                       .header("If-Match", convertToEtag(version)))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Staff with ID=%s not found",
                       "Должность с ID=%s не найдена",
                       STAFF_ID)));

            verify(staffService).delete(STAFF_ID, version);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.staff.StaffControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            mvc.perform(delete(API + "/{id}", STAFF_ID).accept(JSON).locale(locale))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_wrong_etag() throws Exception {
            mvc.perform(delete(API + "/{id}", STAFF_ID).accept(JSON).header("If-Match", "bad"))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.staff.StaffControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected))
                    .when(staffService).delete(STAFF_ID, current);

            mvc.perform(delete(API + "/{id}", STAFF_ID).accept(JSON)
                                                       .locale(locale)
                                                       .header("If-Match", convertToEtag(current)))
               .andExpect(status().isPreconditionFailed());

            verify(staffService).delete(STAFF_ID, current);
        }
    }

    private StaffDTO createDto(Integer id, String position, Long version) {
        return new StaffDTO(id, position, OffsetDateTime.now(), OffsetDateTime.now(), version);
    }

    private StaffPostRequest createPostRequest(String position) {
        StaffPostRequest r = new StaffPostRequest();
        r.setPosition(position);
        return r;
    }

    private StaffPutRequest createPutRequest(String position) {
        StaffPutRequest r = new StaffPutRequest();
        r.setPosition(position);
        return r;
    }

    private StaffPatchRequest createPatchRequest(String position) {
        StaffPatchRequest r = new StaffPatchRequest();
        r.setPosition(position);
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
