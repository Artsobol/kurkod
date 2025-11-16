package io.github.artsobol.kurkod.web.controller.dismissal;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.common.error.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.facade.SecurityContextFacade;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.dismissal.error.DismissalError;
import io.github.artsobol.kurkod.web.domain.dismissal.model.dto.DismissalDTO;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPatchRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPostRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.model.request.DismissalPutRequest;
import io.github.artsobol.kurkod.web.domain.dismissal.service.api.DismissalService;
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
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DismissalController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class DismissalControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/dismissals";
    private static final int WORKER_ID = 10;
    private static final int DISMISSED_ID = 77;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean DismissalService dismissalService;
    @MockitoBean SecurityContextFacade securityContextFacade;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    @Nested
    class GetByWorkerAndDismissed {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            DismissalDTO dto = createDto(1, LocalDate.parse("2025-01-01"), "Reason", "WorkerName", "BossName", 5L);
            when(dismissalService.getByWorkerAndDismissed(WORKER_ID, DISMISSED_ID)).thenReturn(dto);

            mvc.perform(get(API + "/workers/{workerId}/dismissed/{dismissedId}", WORKER_ID, DISMISSED_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(5L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(1))
               .andExpect(jsonPath("$.payload.dismissalDate").value("2025-01-01"))
               .andExpect(jsonPath("$.payload.reason").value("Reason"))
               .andExpect(jsonPath("$.payload.version").value(5));

            verify(dismissalService).getByWorkerAndDismissed(WORKER_ID, DISMISSED_ID);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.dismissal.DismissalControllerTest#locales")
        @DisplayName("404 Not Found")
        void returns404(Locale locale) throws Exception {
            when(dismissalService.getByWorkerAndDismissed(WORKER_ID, DISMISSED_ID))
                    .thenThrow(new NotFoundException(DismissalError.NOT_FOUND_BY_WORKER_AND_DISMISSED, WORKER_ID, DISMISSED_ID));

            mvc.perform(get(API + "/workers/{workerId}/dismissed/{dismissedId}", WORKER_ID, DISMISSED_ID)
                                .accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Dismissal for worker ID=%d and dismissed ID=%d not found",
                       "Увольнение для работника ID=%d и увольняющего ID=%d не найдено",
                       WORKER_ID, DISMISSED_ID)));

            verify(dismissalService).getByWorkerAndDismissed(WORKER_ID, DISMISSED_ID);
        }
    }

    @Nested
    class GetAllByDismissed {
        @Test
        @DisplayName("200 OK: list")
        void returns200_with_list() throws Exception {
            List<DismissalDTO> list = List.of(
                    createDto(1, LocalDate.parse("2025-01-01"), "R1", "W1", "B1", 1L),
                    createDto(2, LocalDate.parse("2025-01-02"), "R2", "W2", "B2", 1L)
                                             );
            when(dismissalService.getAllByDismissed(DISMISSED_ID)).thenReturn(list);

            mvc.perform(get(API + "/dismissed/{dismissedId}", DISMISSED_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(2)))
               .andExpect(jsonPath("$.payload[0].id").value(1))
               .andExpect(jsonPath("$.payload[1].id").value(2));

            verify(dismissalService).getAllByDismissed(DISMISSED_ID);
        }

        @Test
        @DisplayName("200 OK: empty list")
        void returns200_with_empty_list() throws Exception {
            when(dismissalService.getAllByDismissed(DISMISSED_ID)).thenReturn(List.of());

            mvc.perform(get(API + "/dismissed/{dismissedId}", DISMISSED_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(dismissalService).getAllByDismissed(DISMISSED_ID);
        }
    }

    @Nested
    class GetAllByWorker {
        @Test
        @DisplayName("200 OK: list")
        void returns200_with_list() throws Exception {
            List<DismissalDTO> list = List.of(createDto(3, LocalDate.parse("2025-01-03"), "R3", "W3", "B3", 1L));
            when(dismissalService.getAllByWorker(WORKER_ID)).thenReturn(list);

            mvc.perform(get(API + "/workers/{workerId}", WORKER_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(1)))
               .andExpect(jsonPath("$.payload[0].id").value(3));

            verify(dismissalService).getAllByWorker(WORKER_ID);
        }

        @Test
        @DisplayName("200 OK: empty list")
        void returns200_with_empty_list() throws Exception {
            when(dismissalService.getAllByWorker(WORKER_ID)).thenReturn(List.of());

            mvc.perform(get(API + "/workers/{workerId}", WORKER_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(dismissalService).getAllByWorker(WORKER_ID);
        }
    }

    @Nested
    class CreateOne {
        @Test
        @DisplayName("201 Created: body + ETag + Location")
        void returns201_with_payload_etag_and_location() throws Exception {
            when(securityContextFacade.getCurrentUserId()).thenReturn(DISMISSED_ID);
            DismissalDTO dto = createDto(11, LocalDate.parse("2025-01-05"), "Ok", "WorkerName", "BossName", 1L);
            when(dismissalService.create(any(DismissalPostRequest.class))).thenReturn(dto);

            String json = """
                          {"dismissalDate":"2025-01-05","reason":"Ok","workerId":%d}
                          """.formatted(WORKER_ID);

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(json))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(header().string("Location", endsWith(
                       "/api/v1/dismissals/workers/" + WORKER_ID + "/dismissed/" + DISMISSED_ID)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(11))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(dismissalService).create(any(DismissalPostRequest.class));
            verify(securityContextFacade).getCurrentUserId();
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(dismissalService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_body_invalid() throws Exception {
            String json = """
                          {"dismissalDate":"2999-01-01","reason":"","workerId":%d}
                          """.formatted(WORKER_ID);

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(json))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(dismissalService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON).content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(dismissalService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(MediaType.TEXT_PLAIN).content("dismissalDate=2025-01-01"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(dismissalService);
        }
    }

    @Nested
    class ReplaceOne {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long currentVersion = 5L, nextVersion = 6L;
            DismissalDTO dto = createDto(1, LocalDate.parse("2025-02-01"), "New", "W", "B", nextVersion);
            when(dismissalService.replace(eq(WORKER_ID), any(DismissalPutRequest.class), eq(currentVersion))).thenReturn(dto);

            String json = """
                          {"dismissalDate":"2025-02-01","reason":"New"}
                          """;

            mvc.perform(put(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                           .contentType(JSON)
                                                           .header("If-Match", convertToEtag(currentVersion))
                                                           .content(json))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(nextVersion)))
               .andExpect(jsonPath("$.payload.version").value((int) nextVersion));

            verify(dismissalService).replace(eq(WORKER_ID), any(DismissalPutRequest.class), eq(currentVersion));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.dismissal.DismissalControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            String json = """
                          {"dismissalDate":"2025-02-01","reason":"New"}
                          """;

            mvc.perform(put(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                           .locale(locale)
                                                           .contentType(JSON)
                                                           .content(json))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            String json = """
                          {"dismissalDate":"2025-02-01","reason":"New"}
                          """;

            mvc.perform(put(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                           .contentType(JSON)
                                                           .header("If-Match", "trash")
                                                           .content(json))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(put(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                           .contentType(JSON)
                                                           .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(dismissalService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            String json = """
                          {"dismissalDate":"2999-02-01","reason":""}
                          """;

            mvc.perform(put(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                           .contentType(JSON)
                                                           .header("If-Match", convertToEtag(5L))
                                                           .content(json))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(dismissalService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.dismissal.DismissalControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long currentVersion = 5L, expectedVersion = 10L;
            when(dismissalService.replace(eq(WORKER_ID), any(DismissalPutRequest.class), eq(currentVersion)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, currentVersion, expectedVersion));

            String json = """
                          {"dismissalDate":"2025-02-01","reason":"New"}
                          """;

            mvc.perform(put(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                           .locale(locale)
                                                           .contentType(JSON)
                                                           .header("If-Match", convertToEtag(currentVersion))
                                                           .content(json))
               .andExpect(status().isPreconditionFailed());

            verify(dismissalService).replace(eq(WORKER_ID), any(DismissalPutRequest.class), eq(currentVersion));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.dismissal.DismissalControllerTest#locales")
        @DisplayName("404 Not Found")
        void returns404(Locale locale) throws Exception {
            long currentVersion = 5L;
            when(dismissalService.replace(eq(WORKER_ID), any(DismissalPutRequest.class), eq(currentVersion)))
                    .thenThrow(new NotFoundException(DismissalError.NOT_FOUND_BY_WORKER_ID, WORKER_ID));

            String json = """
                          {"dismissalDate":"2025-02-01","reason":"New"}
                          """;

            mvc.perform(put(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                           .locale(locale)
                                                           .contentType(JSON)
                                                           .header("If-Match", convertToEtag(currentVersion))
                                                           .content(json))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Dismissal for worker ID=%s not found",
                       "Увольнение для работника с ID=%s не найдено",
                       WORKER_ID)));

            verify(dismissalService).replace(eq(WORKER_ID), any(DismissalPutRequest.class), eq(currentVersion));
        }
    }

    @Nested
    class UpdateOne {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long currentVersion = 2L, nextVersion = 3L;
            DismissalDTO dto = createDto(1, LocalDate.parse("2025-03-01"), "Patch", "W", "B", nextVersion);
            when(dismissalService.update(eq(WORKER_ID), any(DismissalPatchRequest.class), eq(currentVersion))).thenReturn(dto);

            String json = """
                          {"dismissalDate":"2025-03-01","reason":"Patch"}
                          """;

            mvc.perform(patch(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                             .contentType(JSON)
                                                             .header("If-Match", convertToEtag(currentVersion))
                                                             .content(json))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(nextVersion)))
               .andExpect(jsonPath("$.payload.version").value((int) nextVersion));

            verify(dismissalService).update(eq(WORKER_ID), any(DismissalPatchRequest.class), eq(currentVersion));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.dismissal.DismissalControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            String json = """
                          {"dismissalDate":"2025-03-01","reason":"Patch"}
                          """;

            mvc.perform(patch(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                             .locale(locale)
                                                             .contentType(JSON)
                                                             .content(json))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            String json = """
                          {"dismissalDate":"2025-03-01","reason":"Patch"}
                          """;

            mvc.perform(patch(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                             .contentType(JSON)
                                                             .header("If-Match", "bad")
                                                             .content(json))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                             .contentType(JSON)
                                                             .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(dismissalService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            String json = """
                          {"dismissalDate":"2999-03-01","reason":""}
                          """;

            mvc.perform(patch(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                             .contentType(JSON)
                                                             .header("If-Match", convertToEtag(5L))
                                                             .content(json))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(dismissalService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.dismissal.DismissalControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long currentVersion = 5L, expectedVersion = 10L;
            when(dismissalService.update(eq(WORKER_ID), any(DismissalPatchRequest.class), eq(currentVersion)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, currentVersion, expectedVersion));

            String json = """
                          {"dismissalDate":"2025-03-01","reason":"Patch"}
                          """;

            mvc.perform(patch(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                             .locale(locale)
                                                             .contentType(JSON)
                                                             .header("If-Match", convertToEtag(currentVersion))
                                                             .content(json))
               .andExpect(status().isPreconditionFailed());

            verify(dismissalService).update(eq(WORKER_ID), any(DismissalPatchRequest.class), eq(currentVersion));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.dismissal.DismissalControllerTest#locales")
        @DisplayName("404 Not Found")
        void returns404(Locale locale) throws Exception {
            long currentVersion = 5L;
            when(dismissalService.update(eq(WORKER_ID), any(DismissalPatchRequest.class), eq(currentVersion)))
                    .thenThrow(new NotFoundException(DismissalError.NOT_FOUND_BY_WORKER_ID, WORKER_ID));

            String json = """
                          {"dismissalDate":"2025-03-01","reason":"Patch"}
                          """;

            mvc.perform(patch(API + "/{workerId}", WORKER_ID).accept(JSON)
                                                             .locale(locale)
                                                             .contentType(JSON)
                                                             .header("If-Match", convertToEtag(currentVersion))
                                                             .content(json))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Dismissal for worker ID=%s not found",
                       "Увольнение для работника с ID=%s не найдено",
                       WORKER_ID)));

            verify(dismissalService).update(eq(WORKER_ID), any(DismissalPatchRequest.class), eq(currentVersion));
        }
    }

    private DismissalDTO createDto(Integer id, LocalDate dismissalDate, String reason, String worker, String whoDismiss, Long version) {
        return new DismissalDTO(id, dismissalDate, reason, worker, whoDismiss, version);
    }

    private String convertToEtag(long version) {
        return EtagUtils.toEtag(version);
    }

    static Stream<Locale> locales() {
        return Stream.of(Locale.ENGLISH, Locale.of("ru", "RU"));
    }

    private static String createMessage(Locale locale, String englishPattern, String russianPattern, Object... args) {
        String pattern = locale.getLanguage().equals("ru") ? russianPattern : englishPattern;
        return args.length == 0 ? pattern : String.format(pattern, args);
    }
}
