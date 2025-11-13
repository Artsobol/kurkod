package io.github.artsobol.kurkod.web.controller.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.common.exception.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.worker.error.WorkerError;
import io.github.artsobol.kurkod.web.domain.worker.model.dto.WorkerDTO;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPatchRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPostRequest;
import io.github.artsobol.kurkod.web.domain.worker.model.request.WorkerPutRequest;
import io.github.artsobol.kurkod.web.domain.worker.service.api.WorkerService;
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

@WebMvcTest(controllers = WorkerController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class WorkerControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/workers";
    private static final int WORKER_ID = 42;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean WorkerService workerService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    @Nested
    class GetById {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            WorkerDTO response = createDto(WORKER_ID, "John", "Doe", "P", 1L);
            when(workerService.get(WORKER_ID)).thenReturn(response);

            mvc.perform(get(API + "/{id}", WORKER_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(WORKER_ID))
               .andExpect(jsonPath("$.payload.firstName").value("John"))
               .andExpect(jsonPath("$.payload.lastName").value("Doe"))
               .andExpect(jsonPath("$.payload.patronymic").value("P"))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(workerService).get(WORKER_ID);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.worker.WorkerControllerTest#locales")
        @DisplayName("404 Not Found: no worker with this id")
        void returns404_with_i18n_message(Locale locale) throws Exception {
            when(workerService.get(WORKER_ID))
                    .thenThrow(new NotFoundException(WorkerError.NOT_FOUND_BY_ID, WORKER_ID));

            mvc.perform(get(API + "/{id}", WORKER_ID).accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Worker with ID=%d not found",
                       "Работник с ID=%d не найден",
                       WORKER_ID)));

            verify(workerService).get(WORKER_ID);
        }
    }

    @Nested
    class GetAll {
        @Test
        @DisplayName("200 OK: list entities")
        void returns200_with_list() throws Exception {
            List<WorkerDTO> response = List.of(
                    createDto(1, "A", "B", null, 3L),
                    createDto(2, "C", "D", "E", 4L)
                                              );
            when(workerService.getAll()).thenReturn(response);

            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(2)))
               .andExpect(jsonPath("$.payload[0].id").value(1))
               .andExpect(jsonPath("$.payload[1].id").value(2));

            verify(workerService).getAll();
        }

        @Test
        @DisplayName("200 OK: empty list")
        void returns200_with_empty_list() throws Exception {
            when(workerService.getAll()).thenReturn(List.of());

            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(workerService).getAll();
        }
    }

    @Nested
    class Create {
        @Test
        @DisplayName("201 Created: body + Location + ETag")
        void returns201_with_payload_location_and_etag() throws Exception {
            WorkerPostRequest request = createPostRequest("John", "Doe", "P");
            WorkerDTO response = createDto(7, "John", "Doe", "P", 1L);
            when(workerService.create(any(WorkerPostRequest.class))).thenReturn(response);

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("Location", endsWith(API + "/7")))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(7))
               .andExpect(jsonPath("$.payload.firstName").value("John"));

            verify(workerService).create(any(WorkerPostRequest.class));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(workerService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_body_invalid() throws Exception {
            WorkerPostRequest invalid = createPostRequest("J", "D", "ThisPatronymicIsWayTooLongToPassValidationOver30Chars");

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(invalid)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(workerService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON).content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(workerService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API).accept(JSON)
                                 .contentType(MediaType.TEXT_PLAIN)
                                 .content("firstName=John"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(workerService);
        }
    }

    @Nested
    class Replace {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long oldVersion = 5L;
            WorkerPutRequest request = createPutRequest("Jane", "Doe", "P");
            WorkerDTO response = createDto(WORKER_ID, "Jane", "Doe", "P", 6L);
            when(workerService.replace(eq(WORKER_ID), any(WorkerPutRequest.class), eq(oldVersion))).thenReturn(response);

            mvc.perform(put(API + "/{id}", WORKER_ID).accept(JSON)
                                                     .contentType(JSON)
                                                     .header("If-Match", convertToEtag(oldVersion))
                                                     .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(6L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(WORKER_ID))
               .andExpect(jsonPath("$.payload.version").value(6));

            verify(workerService).replace(eq(WORKER_ID), any(WorkerPutRequest.class), eq(oldVersion));
        }

        @Test
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing() throws Exception {
            WorkerPutRequest request = createPutRequest("Jane", "Doe", "P");

            mvc.perform(put(API + "/{id}", WORKER_ID).accept(JSON)
                                                     .contentType(JSON)
                                                     .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_invalid_etag() throws Exception {
            WorkerPutRequest request = createPutRequest("Jane", "Doe", "P");

            mvc.perform(put(API + "/{id}", WORKER_ID).accept(JSON)
                                                     .contentType(JSON)
                                                     .header("If-Match", "trash")
                                                     .content(convertToJson(request)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(put(API + "/{id}", WORKER_ID).accept(JSON)
                                                     .contentType(JSON)
                                                     .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(workerService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            WorkerPutRequest invalid = createPutRequest("J", "D", "ThisPatronymicIsWayTooLongToPassValidationOver30Chars");

            mvc.perform(put(API + "/{id}", WORKER_ID).accept(JSON)
                                                     .contentType(JSON)
                                                     .header("If-Match", convertToEtag(5L))
                                                     .content(convertToJson(invalid)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(workerService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(put(API + "/{id}", WORKER_ID).accept(JSON)
                                                     .contentType(MediaType.TEXT_PLAIN)
                                                     .header("If-Match", convertToEtag(5L))
                                                     .content("firstName=Jane"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(workerService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.worker.WorkerControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L;
            long expected = 10L;
            when(workerService.replace(eq(WORKER_ID), any(WorkerPutRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            WorkerPutRequest request = createPutRequest("Jane", "Doe", "P");
            mvc.perform(put(API + "/{id}", WORKER_ID).accept(JSON)
                                                     .locale(locale)
                                                     .contentType(JSON)
                                                     .header("If-Match", convertToEtag(current))
                                                     .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(workerService).replace(eq(WORKER_ID), any(WorkerPutRequest.class), eq(current));
        }
    }

    @Nested
    class Update {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long oldVersion = 5L;
            WorkerPatchRequest request = createPatchRequest("J", null, null);
            WorkerDTO response = createDto(WORKER_ID, "J", "Doe", "P", 6L);
            when(workerService.update(eq(WORKER_ID), any(WorkerPatchRequest.class), eq(oldVersion))).thenReturn(response);

            mvc.perform(patch(API + "/{id}", WORKER_ID).accept(JSON)
                                                       .contentType(JSON)
                                                       .header("If-Match", convertToEtag(oldVersion))
                                                       .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(6L)))
               .andExpect(jsonPath("$.payload.id").value(WORKER_ID));

            verify(workerService).update(eq(WORKER_ID), any(WorkerPatchRequest.class), eq(oldVersion));
        }

        @Test
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing() throws Exception {
            WorkerPatchRequest request = createPatchRequest("Jane", null, null);

            mvc.perform(patch(API + "/{id}", WORKER_ID).accept(JSON)
                                                       .contentType(JSON)
                                                       .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API + "/{id}", WORKER_ID).accept(JSON)
                                                       .contentType(JSON)
                                                       .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(workerService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match header")
        void returns400_when_invalid_etag() throws Exception {
            WorkerPatchRequest request = createPatchRequest("Jane", null, null);

            mvc.perform(patch(API + "/{id}", WORKER_ID).accept(JSON)
                                                       .contentType(JSON)
                                                       .header("If-Match", "trash")
                                                       .content(convertToJson(request)))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.worker.WorkerControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L;
            long expected = 10L;
            when(workerService.update(eq(WORKER_ID), any(WorkerPatchRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            WorkerPatchRequest request = createPatchRequest("Jane", null, null);
            mvc.perform(patch(API + "/{id}", WORKER_ID).accept(JSON)
                                                       .locale(locale)
                                                       .contentType(JSON)
                                                       .header("If-Match", convertToEtag(current))
                                                       .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(workerService).update(eq(WORKER_ID), any(WorkerPatchRequest.class), eq(current));
        }
    }

    @Nested
    class Delete {
        @Test
        @DisplayName("204 No Content: empty body")
        void returns204() throws Exception {
            long version = 5L;
            doNothing().when(workerService).delete(WORKER_ID, version);

            mvc.perform(delete(API + "/{id}", WORKER_ID).accept(JSON).header("If-Match", convertToEtag(version)))
               .andExpect(status().isNoContent())
               .andExpect(content().string(""));

            verify(workerService).delete(WORKER_ID, version);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.worker.WorkerControllerTest#locales")
        @DisplayName("404 Not Found")
        void returns404_when_not_found(Locale locale) throws Exception {
            long version = 5L;
            doThrow(new NotFoundException(WorkerError.NOT_FOUND_BY_ID, WORKER_ID))
                    .when(workerService).delete(WORKER_ID, version);

            mvc.perform(delete(API + "/{id}", WORKER_ID).accept(JSON)
                                                        .locale(locale)
                                                        .header("If-Match", convertToEtag(version)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Worker with ID=%s not found",
                       "Работник с ID=%s не найден",
                       WORKER_ID)));

            verify(workerService).delete(WORKER_ID, version);
        }

        @Test
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing() throws Exception {
            mvc.perform(delete(API + "/{id}", WORKER_ID).accept(JSON))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match header")
        void returns400_when_wrong_etag() throws Exception {
            mvc.perform(delete(API + "/{id}", WORKER_ID).accept(JSON).header("If-Match", "bad"))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.worker.WorkerControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L;
            long expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected))
                    .when(workerService).delete(WORKER_ID, current);

            mvc.perform(delete(API + "/{id}", WORKER_ID).accept(JSON)
                                                        .locale(locale)
                                                        .header("If-Match", convertToEtag(current)))
               .andExpect(status().isPreconditionFailed());

            verify(workerService).delete(WORKER_ID, current);
        }
    }

    private WorkerDTO createDto(int id, String firstName, String lastName, String patronymic, long version) {
        return new WorkerDTO(id, firstName, lastName, patronymic, version);
    }

    private WorkerPostRequest createPostRequest(String firstName, String lastName, String patronymic) {
        WorkerPostRequest request = new WorkerPostRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPatronymic(patronymic);
        return request;
    }

    private WorkerPutRequest createPutRequest(String firstName, String lastName, String patronymic) {
        WorkerPutRequest request = new WorkerPutRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPatronymic(patronymic);
        return request;
    }

    private WorkerPatchRequest createPatchRequest(String firstName, String lastName, String patronymic) {
        WorkerPatchRequest request = new WorkerPatchRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setPatronymic(patronymic);
        return request;
    }

    private String convertToJson(Object object) throws Exception {
        return objectMapper.writeValueAsString(object);
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
