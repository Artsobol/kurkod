package io.github.artsobol.kurkod.web.controller.rows;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.common.error.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.rows.error.RowsError;
import io.github.artsobol.kurkod.web.domain.rows.model.dto.RowsDTO;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPatchRequest;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPostRequest;
import io.github.artsobol.kurkod.web.domain.rows.model.request.RowsPutRequest;
import io.github.artsobol.kurkod.web.domain.rows.service.api.RowsService;
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

@WebMvcTest(controllers = RowsController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class RowsControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/workshops/{workshopId}/rows";
    private static final int WORKSHOP_ID = 1;
    private static final int ROW_NUMBER = 2;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean RowsService rowsService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    @Nested
    class GetOne {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            RowsDTO response = createDto(101, ROW_NUMBER, WORKSHOP_ID, 7L);
            when(rowsService.find(WORKSHOP_ID, ROW_NUMBER)).thenReturn(response);

            mvc.perform(get(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(7L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(101))
               .andExpect(jsonPath("$.payload.rowNumber").value(ROW_NUMBER))
               .andExpect(jsonPath("$.payload.workshopNumber").value(WORKSHOP_ID))
               .andExpect(jsonPath("$.payload.version").value(7));

            verify(rowsService).find(WORKSHOP_ID, ROW_NUMBER);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.rows.RowsControllerTest#locales")
        @DisplayName("404 Not Found: not found by keys")
        void returns404_with_i18n(Locale locale) throws Exception {
            when(rowsService.find(WORKSHOP_ID, ROW_NUMBER))
                    .thenThrow(new NotFoundException(RowsError.NOT_FOUND_BY_KEYS, WORKSHOP_ID, ROW_NUMBER));

            mvc.perform(get(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Row for workshopId=%d, rowNumber=%d not found",
                       "Ряд для workshopId=%d, номер ряда=%d не найден",
                       WORKSHOP_ID, ROW_NUMBER)));

            verify(rowsService).find(WORKSHOP_ID, ROW_NUMBER);
        }
    }

    @Nested
    class GetAll {
        @Test
        @DisplayName("200 OK: list")
        void returns200_with_list() throws Exception {
            List<RowsDTO> response = List.of(
                    createDto(1, 1, WORKSHOP_ID, 1L),
                    createDto(2, 2, WORKSHOP_ID, 1L)
                                            );
            when(rowsService.findAll(WORKSHOP_ID)).thenReturn(response);

            mvc.perform(get(API, WORKSHOP_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(2)))
               .andExpect(jsonPath("$.payload[0].rowNumber").value(1))
               .andExpect(jsonPath("$.payload[1].rowNumber").value(2));

            verify(rowsService).findAll(WORKSHOP_ID);
        }

        @Test
        @DisplayName("200 OK: empty list")
        void returns200_with_empty_list() throws Exception {
            when(rowsService.findAll(WORKSHOP_ID)).thenReturn(List.of());

            mvc.perform(get(API, WORKSHOP_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(rowsService).findAll(WORKSHOP_ID);
        }
    }

    @Nested
    class Create {
        @Test
        @DisplayName("201 Created: body + ETag + Location")
        void returns201_with_payload_etag_and_location() throws Exception {
            RowsPostRequest request = createPostRequest(3);
            RowsDTO response = createDto(201, 3, WORKSHOP_ID, 1L);
            when(rowsService.create(eq(WORKSHOP_ID), any(RowsPostRequest.class))).thenReturn(response);

            mvc.perform(post(API, WORKSHOP_ID).accept(JSON).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(header().string("Location", endsWith("/api/v1/workshops/" + WORKSHOP_ID + "/rows/3")))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(201))
               .andExpect(jsonPath("$.payload.rowNumber").value(3))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(rowsService).create(eq(WORKSHOP_ID), any(RowsPostRequest.class));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.rows.RowsControllerTest#locales")
        @DisplayName("409 Conflict: already exists")
        void returns409_when_already_exists(Locale locale) throws Exception {
            RowsPostRequest request = createPostRequest(ROW_NUMBER);
            when(rowsService.create(eq(WORKSHOP_ID), any(RowsPostRequest.class)))
                    .thenThrow(new DataExistException(RowsError.ALREADY_EXISTS, WORKSHOP_ID, ROW_NUMBER));

            mvc.perform(post(API, WORKSHOP_ID).accept(JSON).locale(locale).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isConflict())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Row already exists for workshopId=%d, rowNumber=%d",
                       "Ряд уже существует для workshopId=%d, номер ряда=%d",
                       WORKSHOP_ID, ROW_NUMBER)));

            verify(rowsService).create(eq(WORKSHOP_ID), any(RowsPostRequest.class));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API, WORKSHOP_ID).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(rowsService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_body_invalid() throws Exception {
            RowsPostRequest invalid = createPostRequest(-1);

            mvc.perform(post(API, WORKSHOP_ID).accept(JSON).contentType(JSON).content(convertToJson(invalid)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(rowsService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API, WORKSHOP_ID).accept(JSON).contentType(JSON).content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(rowsService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API, WORKSHOP_ID).accept(JSON).contentType(MediaType.TEXT_PLAIN).content("rowNumber=3"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(rowsService);
        }
    }

    @Nested
    class Replace {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long current = 5L, next = 6L;
            RowsPutRequest request = createPutRequest(4);
            RowsDTO response = createDto(101, 4, WORKSHOP_ID, next);
            when(rowsService.replace(eq(WORKSHOP_ID), eq(ROW_NUMBER), any(RowsPutRequest.class), eq(current)))
                    .thenReturn(response);

            mvc.perform(put(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                          .contentType(JSON)
                                                                          .header("If-Match", convertToEtag(current))
                                                                          .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(next)))
               .andExpect(jsonPath("$.payload.version").value((int) next));

            verify(rowsService).replace(eq(WORKSHOP_ID), eq(ROW_NUMBER), any(RowsPutRequest.class), eq(current));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.rows.RowsControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            RowsPutRequest request = createPutRequest(4);

            mvc.perform(put(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                          .locale(locale)
                                                                          .contentType(JSON)
                                                                          .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            RowsPutRequest request = createPutRequest(4);

            mvc.perform(put(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                          .contentType(JSON)
                                                                          .header("If-Match", "trash")
                                                                          .content(convertToJson(request)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(put(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                          .contentType(JSON)
                                                                          .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(rowsService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            RowsPutRequest invalid = createPutRequest(-10);

            mvc.perform(put(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                          .contentType(JSON)
                                                                          .header("If-Match", convertToEtag(5L))
                                                                          .content(convertToJson(invalid)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(rowsService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(put(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                          .contentType(JSON)
                                                                          .header("If-Match", convertToEtag(5L))
                                                                          .content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(rowsService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.rows.RowsControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(rowsService.replace(eq(WORKSHOP_ID), eq(ROW_NUMBER), any(RowsPutRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            RowsPutRequest request = createPutRequest(4);
            mvc.perform(put(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                          .locale(locale)
                                                                          .contentType(JSON)
                                                                          .header("If-Match", convertToEtag(current))
                                                                          .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(rowsService).replace(eq(WORKSHOP_ID), eq(ROW_NUMBER), any(RowsPutRequest.class), eq(current));
        }
    }

    @Nested
    class Update {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long current = 2L, next = 3L;
            RowsPatchRequest request = createPatchRequest(9);
            RowsDTO response = createDto(101, 9, WORKSHOP_ID, next);
            when(rowsService.update(eq(WORKSHOP_ID), eq(ROW_NUMBER), any(RowsPatchRequest.class), eq(current)))
                    .thenReturn(response);

            mvc.perform(patch(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                            .contentType(JSON)
                                                                            .header("If-Match", convertToEtag(current))
                                                                            .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(next)))
               .andExpect(jsonPath("$.payload.rowNumber").value(9))
               .andExpect(jsonPath("$.payload.version").value((int) next));

            verify(rowsService).update(eq(WORKSHOP_ID), eq(ROW_NUMBER), any(RowsPatchRequest.class), eq(current));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.rows.RowsControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            RowsPatchRequest request = createPatchRequest(8);

            mvc.perform(patch(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                            .locale(locale)
                                                                            .contentType(JSON)
                                                                            .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            RowsPatchRequest request = createPatchRequest(8);

            mvc.perform(patch(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                            .contentType(JSON)
                                                                            .header("If-Match", "bad")
                                                                            .content(convertToJson(request)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                            .contentType(JSON)
                                                                            .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(rowsService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            RowsPatchRequest invalid = createPatchRequest(-1);

            mvc.perform(patch(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                            .contentType(JSON)
                                                                            .header("If-Match", convertToEtag(5L))
                                                                            .content(convertToJson(invalid)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(rowsService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.rows.RowsControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(rowsService.update(eq(WORKSHOP_ID), eq(ROW_NUMBER), any(RowsPatchRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            RowsPatchRequest request = createPatchRequest(8);
            mvc.perform(patch(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                            .locale(locale)
                                                                            .contentType(JSON)
                                                                            .header("If-Match", convertToEtag(current))
                                                                            .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(rowsService).update(eq(WORKSHOP_ID), eq(ROW_NUMBER), any(RowsPatchRequest.class), eq(current));
        }
    }

    @Nested
    class DeleteOne {
        @Test
        @DisplayName("204 No Content: no ETag in response")
        void returns204_without_etag() throws Exception {
            long v = 5L;
            doNothing().when(rowsService).delete(WORKSHOP_ID, ROW_NUMBER, v);

            mvc.perform(delete(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                             .header("If-Match", convertToEtag(v)))
               .andExpect(status().isNoContent())
               .andExpect(header().doesNotExist("ETag"))
               .andExpect(content().string(""));

            verify(rowsService).delete(WORKSHOP_ID, ROW_NUMBER, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.rows.RowsControllerTest#locales")
        @DisplayName("404 Not Found")
        void returns404(Locale locale) throws Exception {
            long v = 5L;
            doThrow(new NotFoundException(RowsError.NOT_FOUND_BY_KEYS, WORKSHOP_ID, ROW_NUMBER))
                    .when(rowsService).delete(WORKSHOP_ID, ROW_NUMBER, v);

            mvc.perform(delete(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                             .locale(locale)
                                                                             .header("If-Match", convertToEtag(v)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Row for workshopId=%s, rowNumber=%s not found",
                       "Ряд для workshopId=%s, номер ряда=%s не найден",
                       WORKSHOP_ID, ROW_NUMBER)));

            verify(rowsService).delete(WORKSHOP_ID, ROW_NUMBER, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.rows.RowsControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            mvc.perform(delete(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON).locale(locale))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_wrong_etag() throws Exception {
            mvc.perform(delete(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON).header("If-Match", "bad"))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.rows.RowsControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected))
                    .when(rowsService).delete(WORKSHOP_ID, ROW_NUMBER, current);

            mvc.perform(delete(API + "/{rowNumber}", WORKSHOP_ID, ROW_NUMBER).accept(JSON)
                                                                             .locale(locale)
                                                                             .header("If-Match", convertToEtag(current)))
               .andExpect(status().isPreconditionFailed());

            verify(rowsService).delete(WORKSHOP_ID, ROW_NUMBER, current);
        }
    }

    private RowsDTO createDto(Integer id, Integer rowNumber, Integer workshopNumber, Long version) {
        return new RowsDTO(id, rowNumber, workshopNumber, null, null, version);
    }

    private RowsPostRequest createPostRequest(Integer rowNumber) {
        RowsPostRequest request = new RowsPostRequest();
        request.setRowNumber(rowNumber);
        return request;
    }

    private RowsPutRequest createPutRequest(Integer rowNumber) {
        RowsPutRequest request = new RowsPutRequest();
        request.setRowNumber(rowNumber);
        return request;
    }

    private RowsPatchRequest createPatchRequest(Integer rowNumber) {
        RowsPatchRequest request = new RowsPatchRequest();
        request.setRowNumber(rowNumber);
        return request;
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
