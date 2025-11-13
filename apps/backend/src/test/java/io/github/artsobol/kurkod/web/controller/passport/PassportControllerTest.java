package io.github.artsobol.kurkod.web.controller.passport;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.common.exception.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.passport.model.dto.PassportDTO;
import io.github.artsobol.kurkod.web.domain.passport.model.request.PassportPatchRequest;
import io.github.artsobol.kurkod.web.domain.passport.model.request.PassportPostRequest;
import io.github.artsobol.kurkod.web.domain.passport.model.request.PassportPutRequest;
import io.github.artsobol.kurkod.web.domain.passport.service.api.PassportService;
import lombok.SneakyThrows;
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

@WebMvcTest(controllers = PassportController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class PassportControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/workers/{workerId}/passport";
    private static final int WORKER_ID = 5;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean PassportService passportService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    @Nested
    class GetByWorkerId {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            PassportDTO dto = createDto("1234", "123456", 1L);
            when(passportService.get(WORKER_ID)).thenReturn(dto);

            mvc.perform(get(API, WORKER_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.series").value("1234"))
               .andExpect(jsonPath("$.payload.number").value("123456"))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(passportService).get(WORKER_ID);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.passport.PassportControllerTest#locales")
        @DisplayName("404 Not Found: no passport for worker")
        void returns404_with_i18n_message(Locale locale) throws Exception {
            when(passportService.get(WORKER_ID)).thenThrow(new NotFoundException(
                    io.github.artsobol.kurkod.web.domain.passport.error.PassportError.NOT_FOUND_BY_WORKER_ID,
                    WORKER_ID
            ));

            mvc.perform(get(API, WORKER_ID).accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Passport for worker ID=%d not found",
                       "Паспорт для работника с ID=%d не найден",
                       WORKER_ID)));

            verify(passportService).get(WORKER_ID);
        }
    }

    @Nested
    class Create {
        @Test
        @DisplayName("201 Created: body + Location")
        void returns201_with_payload_and_location() throws Exception {
            PassportPostRequest request = createPostRequest("1234", "123456");
            PassportDTO dto = createDto("1234", "123456", 1L);
            when(passportService.create(eq(WORKER_ID), any(PassportPostRequest.class))).thenReturn(dto);

            mvc.perform(post(API, WORKER_ID).accept(JSON).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("Location", endsWith("/api/v1/workers/" + WORKER_ID + "/passport")))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.series").value("1234"))
               .andExpect(jsonPath("$.payload.number").value("123456"));

            verify(passportService).create(eq(WORKER_ID), any(PassportPostRequest.class));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.passport.PassportControllerTest#locales")
        @SneakyThrows
        @DisplayName("409 Conflict: passport already exists")
        void returns409_when_passport_exists(Locale locale){
            PassportPostRequest request = createPostRequest("1234", "123456");
            when(passportService.create(eq(WORKER_ID), any(PassportPostRequest.class)))
                    .thenThrow(new DataExistException(
                            io.github.artsobol.kurkod.web.domain.passport.error.PassportError.ALREADY_EXISTS,
                            WORKER_ID));

            mvc.perform(post(API, WORKER_ID).accept(JSON).contentType(JSON).content(convertToJson(request)).locale(locale))
               .andExpect(status().isConflict())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Passport already exists for worker ID=%d",
                       "Паспорт уже существует для работника с ID=%d",
                       WORKER_ID)));

            verify(passportService).create(eq(WORKER_ID), any(PassportPostRequest.class));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API, WORKER_ID).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(passportService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_body_invalid() throws Exception {
            PassportPostRequest bad = new PassportPostRequest("", "12"); // нарушает валидации
            mvc.perform(post(API, WORKER_ID).accept(JSON).contentType(JSON).content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(passportService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API, WORKER_ID).accept(JSON).contentType(JSON).content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(passportService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API, WORKER_ID).accept(JSON)
                                            .contentType(MediaType.TEXT_PLAIN)
                                            .content("series=1234&number=123456"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(passportService);
        }
    }

    @Nested
    class Replace {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_new_etag() throws Exception {
            long oldV = 5L, newV = 6L;
            PassportPutRequest request = createPutRequest("2222", "654321");
            PassportDTO dto = createDto("2222", "654321", newV);
            when(passportService.replace(eq(WORKER_ID), any(PassportPutRequest.class), eq(oldV))).thenReturn(dto);

            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .contentType(JSON)
                                           .header("If-Match", convertToEtag(oldV))
                                           .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(newV)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.version").value((int)newV));

            verify(passportService).replace(eq(WORKER_ID), any(PassportPutRequest.class), eq(oldV));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.passport.PassportControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            PassportPutRequest request = createPutRequest("1111", "111111");

            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .locale(locale)
                                           .contentType(JSON)
                                           .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.passport.PassportControllerTest#locales")
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_invalid_etag(Locale locale) throws Exception {
            PassportPutRequest request = createPutRequest("1111", "111111");

            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .locale(locale)
                                           .contentType(JSON)
                                           .header("If-Match", "trash")
                                           .content(convertToJson(request)))
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

            verifyNoInteractions(passportService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            PassportPutRequest bad = new PassportPutRequest(null, null);
            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .contentType(JSON)
                                           .header("If-Match", convertToEtag(5L))
                                           .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(passportService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .contentType(JSON)
                                           .header("If-Match", convertToEtag(5L))
                                           .content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(passportService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .contentType(MediaType.TEXT_PLAIN)
                                           .header("If-Match", convertToEtag(5L))
                                           .content("series=1111&number=111111"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(passportService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.passport.PassportControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(passportService.replace(eq(WORKER_ID), any(PassportPutRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            PassportPutRequest request = createPutRequest("2222", "222222");
            mvc.perform(put(API, WORKER_ID).accept(JSON)
                                           .locale(locale)
                                           .contentType(JSON)
                                           .header("If-Match", convertToEtag(current))
                                           .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(passportService).replace(eq(WORKER_ID), any(PassportPutRequest.class), eq(current));
        }
    }

    @Nested
    class Update {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_new_etag() throws Exception {
            long oldV = 5L, newV = 6L;
            PassportPatchRequest request = createPatchRequest("3333", null);
            PassportDTO dto = createDto("3333", "123456", newV);
            when(passportService.update(eq(WORKER_ID), any(PassportPatchRequest.class), eq(oldV))).thenReturn(dto);

            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .contentType(JSON)
                                             .header("If-Match", convertToEtag(oldV))
                                             .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(newV)))
               .andExpect(jsonPath("$.payload.version").value((int)newV));

            verify(passportService).update(eq(WORKER_ID), any(PassportPatchRequest.class), eq(oldV));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .contentType(JSON)
                                             .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(passportService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            PassportPatchRequest bad = new PassportPatchRequest("aaaa", "1");
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .contentType(JSON)
                                             .header("If-Match", convertToEtag(5L))
                                             .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(passportService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception{
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .contentType(JSON)
                                             .header("If-Match", convertToEtag(5L))
                                             .content("["))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(passportService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .contentType(MediaType.TEXT_PLAIN)
                                             .header("If-Match", convertToEtag(5L))
                                             .content("series=3333"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(passportService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.passport.PassportControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            PassportPatchRequest request = createPatchRequest("4444", null);
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .locale(locale)
                                             .contentType(JSON)
                                             .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.passport.PassportControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(passportService.update(eq(WORKER_ID), any(PassportPatchRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            PassportPatchRequest request = createPatchRequest("5555", null);
            mvc.perform(patch(API, WORKER_ID).accept(JSON)
                                             .locale(locale)
                                             .contentType(JSON)
                                             .header("If-Match", convertToEtag(current))
                                             .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(passportService).update(eq(WORKER_ID), any(PassportPatchRequest.class), eq(current));
        }
    }

    @Nested
    class Delete {
        @Test
        @DisplayName("204 No Content: no ETag in response")
        void returns204_without_etag() throws Exception {
            long v = 5L;
            doNothing().when(passportService).delete(WORKER_ID, v);

            mvc.perform(delete(API, WORKER_ID).accept(JSON).header("If-Match", convertToEtag(v)))
               .andExpect(status().isNoContent())
               .andExpect(header().doesNotExist("ETag"))
               .andExpect(content().string(""));

            verify(passportService).delete(WORKER_ID, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.passport.PassportControllerTest#locales")
        @DisplayName("404 Not Found: not found")
        void returns404_when_not_found(Locale locale) throws Exception {
            long v = 5L;
            doThrow(new NotFoundException(
                    io.github.artsobol.kurkod.web.domain.passport.error.PassportError.NOT_FOUND_BY_WORKER_ID,
                    WORKER_ID)).when(passportService).delete(WORKER_ID, v);

            mvc.perform(delete(API, WORKER_ID).accept(JSON).locale(locale).header("If-Match", convertToEtag(v)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Passport for worker ID=%s not found",
                       "Паспорт для работника с ID=%s не найден",
                       WORKER_ID)));

            verify(passportService).delete(WORKER_ID, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.passport.PassportControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            mvc.perform(delete(API, WORKER_ID).accept(JSON).locale(locale))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.passport.PassportControllerTest#locales")
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_wrong_etag(Locale locale) throws Exception {
            mvc.perform(delete(API, WORKER_ID).accept(JSON).locale(locale).header("If-Match", "bad"))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.passport.PassportControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected))
                    .when(passportService).delete(WORKER_ID, current);

            mvc.perform(delete(API, WORKER_ID).accept(JSON)
                                              .locale(locale)
                                              .header("If-Match", convertToEtag(current)))
               .andExpect(status().isPreconditionFailed());

            verify(passportService).delete(WORKER_ID, current);
        }
    }

    // -------- helpers --------

    private PassportDTO createDto(String series, String number, long version) {
        return new PassportDTO(series, number, OffsetDateTime.now(), OffsetDateTime.now(), version);
    }

    private PassportPostRequest createPostRequest(String series, String number) {
        return new PassportPostRequest(series, number);
    }

    private PassportPutRequest createPutRequest(String series, String number) {
        return new PassportPutRequest(series, number);
    }

    private PassportPatchRequest createPatchRequest(String series, String number) {
        return new PassportPatchRequest(series, number);
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
