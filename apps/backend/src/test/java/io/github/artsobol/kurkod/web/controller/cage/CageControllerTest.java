package io.github.artsobol.kurkod.web.controller.cage;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.common.error.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.cage.error.CageError;
import io.github.artsobol.kurkod.web.domain.cage.model.dto.CageDTO;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePatchRequest;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePostRequest;
import io.github.artsobol.kurkod.web.domain.cage.model.request.CagePutRequest;
import io.github.artsobol.kurkod.web.domain.cage.service.api.CageService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CageController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class CageControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/rows/{rowId}/cage";
    private static final int ROW_ID = 1;
    private static final int CAGE_NUMBER = 2;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean CageService cageService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    @Nested
    class Get {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            CageDTO response = createDto(101, CAGE_NUMBER, ROW_ID, 5L);
            when(cageService.find(ROW_ID, CAGE_NUMBER)).thenReturn(response);

            mvc.perform(get(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(5L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(101))
               .andExpect(jsonPath("$.payload.cageNumber").value(CAGE_NUMBER))
               .andExpect(jsonPath("$.payload.rowNumber").value(ROW_ID))
               .andExpect(jsonPath("$.payload.version").value(5));

            verify(cageService).find(ROW_ID, CAGE_NUMBER);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.cage.CageControllerTest#locales")
        @DisplayName("404 Not Found + i18n message")
        void returns404_with_i18n_message(Locale locale) throws Exception {
            when(cageService.find(ROW_ID, CAGE_NUMBER))
                    .thenThrow(new NotFoundException(CageError.NOT_FOUND_BY_KEYS, ROW_ID, CAGE_NUMBER));

            mvc.perform(get(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Cage with workshopId=%d and cageNumber=%d not found",
                       "Клетка с workshopId=%d и cageNumber=%d не найдена",
                       ROW_ID, CAGE_NUMBER)));

            verify(cageService).find(ROW_ID, CAGE_NUMBER);
        }
    }

    @Nested
    class GetAll {
        @Test
        @DisplayName("200 OK: list")
        void returns200_with_list() throws Exception {
            List<CageDTO> response = List.of(
                    createDto(1, 1, ROW_ID, 1L),
                    createDto(2, 2, ROW_ID, 1L)
                                            );
            when(cageService.findAll(ROW_ID)).thenReturn(response);

            mvc.perform(get(API, ROW_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(2)))
               .andExpect(jsonPath("$.payload[0].cageNumber").value(1))
               .andExpect(jsonPath("$.payload[1].cageNumber").value(2));

            verify(cageService).findAll(ROW_ID);
        }

        @Test
        @DisplayName("200 OK: empty list")
        void returns200_with_empty_list() throws Exception {
            when(cageService.findAll(ROW_ID)).thenReturn(List.of());

            mvc.perform(get(API, ROW_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(cageService).findAll(ROW_ID);
        }
    }

    @Nested
    class Create {
        @Test
        @DisplayName("201 Created: body + ETag + Location")
        void returns201_with_payload_etag_and_location() throws Exception {
            CagePostRequest request = createPostRequest(3);
            CageDTO response = createDto(201, 3, ROW_ID, 1L);
            when(cageService.create(eq(ROW_ID), any(CagePostRequest.class))).thenReturn(response);

            mvc.perform(post(API, ROW_ID).accept(JSON).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(header().string("Location", endsWith("/api/v1/rows/" + ROW_ID + "/cage/3")))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(201))
               .andExpect(jsonPath("$.payload.cageNumber").value(3))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(cageService).create(eq(ROW_ID), any(CagePostRequest.class));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.cage.CageControllerTest#locales")
        @DisplayName("409 Conflict + i18n message")
        void returns409_when_already_exists(Locale locale) throws Exception {
            CagePostRequest request = createPostRequest(CAGE_NUMBER);
            when(cageService.create(eq(ROW_ID), any(CagePostRequest.class)))
                    .thenThrow(new DataExistException(CageError.ALREADY_EXISTS, ROW_ID, CAGE_NUMBER));

            mvc.perform(post(API, ROW_ID).accept(JSON).locale(locale).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isConflict())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Cage already exists with workshopId=%d and cageNumber=%d",
                       "Клетка уже существует с workshopId=%d и cageNumber=%d",
                       ROW_ID, CAGE_NUMBER)));

            verify(cageService).create(eq(ROW_ID), any(CagePostRequest.class));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API, ROW_ID).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(cageService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_body_invalid() throws Exception {
            CagePostRequest invalid = createPostRequest(-1);

            mvc.perform(post(API, ROW_ID).accept(JSON).contentType(JSON).content(convertToJson(invalid)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(cageService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API, ROW_ID).accept(JSON).contentType(JSON).content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(cageService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API, ROW_ID).accept(JSON).contentType(MediaType.TEXT_PLAIN).content("cageNumber=3"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(cageService);
        }
    }

    @Nested
    class Replace {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long current = 5L, next = 6L;
            CagePutRequest request = createPutRequest(4);
            CageDTO response = createDto(101, 4, ROW_ID, next);
            when(cageService.replace(eq(ROW_ID), eq(CAGE_NUMBER), any(CagePutRequest.class), eq(current)))
                    .thenReturn(response);

            mvc.perform(put(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                       .contentType(JSON)
                                                                       .header("If-Match", convertToEtag(current))
                                                                       .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(next)))
               .andExpect(jsonPath("$.payload.version").value((int) next));

            verify(cageService).replace(eq(ROW_ID), eq(CAGE_NUMBER), any(CagePutRequest.class), eq(current));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.cage.CageControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            CagePutRequest request = createPutRequest(4);

            mvc.perform(put(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                       .locale(locale)
                                                                       .contentType(JSON)
                                                                       .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            CagePutRequest request = createPutRequest(4);

            mvc.perform(put(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                       .contentType(JSON)
                                                                       .header("If-Match", "trash")
                                                                       .content(convertToJson(request)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(put(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                       .contentType(JSON)
                                                                       .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(cageService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            CagePutRequest invalid = createPutRequest(-10);

            mvc.perform(put(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                       .contentType(JSON)
                                                                       .header("If-Match", convertToEtag(5L))
                                                                       .content(convertToJson(invalid)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(cageService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.cage.CageControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(cageService.replace(eq(ROW_ID), eq(CAGE_NUMBER), any(CagePutRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            CagePutRequest request = createPutRequest(4);
            mvc.perform(put(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                       .locale(locale)
                                                                       .contentType(JSON)
                                                                       .header("If-Match", convertToEtag(current))
                                                                       .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(cageService).replace(eq(ROW_ID), eq(CAGE_NUMBER), any(CagePutRequest.class), eq(current));
        }
    }

    @Nested
    class Update {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long current = 2L, next = 3L;
            CagePatchRequest request = createPatchRequest(9);
            CageDTO response = createDto(101, 9, ROW_ID, next);
            when(cageService.update(eq(ROW_ID), eq(CAGE_NUMBER), any(CagePatchRequest.class), eq(current)))
                    .thenReturn(response);

            mvc.perform(patch(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                         .contentType(JSON)
                                                                         .header("If-Match", convertToEtag(current))
                                                                         .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(next)))
               .andExpect(jsonPath("$.payload.cageNumber").value(9))
               .andExpect(jsonPath("$.payload.version").value((int) next));

            verify(cageService).update(eq(ROW_ID), eq(CAGE_NUMBER), any(CagePatchRequest.class), eq(current));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.cage.CageControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            CagePatchRequest request = createPatchRequest(8);

            mvc.perform(patch(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                         .locale(locale)
                                                                         .contentType(JSON)
                                                                         .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            CagePatchRequest request = createPatchRequest(8);

            mvc.perform(patch(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                         .contentType(JSON)
                                                                         .header("If-Match", "bad")
                                                                         .content(convertToJson(request)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                         .contentType(JSON)
                                                                         .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(cageService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            CagePatchRequest invalid = createPatchRequest(-1);

            mvc.perform(patch(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                         .contentType(JSON)
                                                                         .header("If-Match", convertToEtag(5L))
                                                                         .content(convertToJson(invalid)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(cageService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.cage.CageControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(cageService.update(eq(ROW_ID), eq(CAGE_NUMBER), any(CagePatchRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            CagePatchRequest request = createPatchRequest(8);
            mvc.perform(patch(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                         .locale(locale)
                                                                         .contentType(JSON)
                                                                         .header("If-Match", convertToEtag(current))
                                                                         .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(cageService).update(eq(ROW_ID), eq(CAGE_NUMBER), any(CagePatchRequest.class), eq(current));
        }
    }

    @Nested
    class Delete {
        @Test
        @DisplayName("204 No Content")
        void returns204() throws Exception {
            long version = 5L;
            doNothing().when(cageService).delete(ROW_ID, CAGE_NUMBER, version);

            mvc.perform(delete(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                          .header("If-Match", convertToEtag(version)))
               .andExpect(status().isNoContent())
               .andExpect(header().doesNotExist("ETag"))
               .andExpect(content().string(""));

            verify(cageService).delete(ROW_ID, CAGE_NUMBER, version);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.cage.CageControllerTest#locales")
        @DisplayName("404 Not Found + i18n message")
        void returns404(Locale locale) throws Exception {
            long version = 5L;
            doThrow(new NotFoundException(CageError.NOT_FOUND_BY_KEYS, ROW_ID, CAGE_NUMBER))
                    .when(cageService).delete(ROW_ID, CAGE_NUMBER, version);

            mvc.perform(delete(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                          .locale(locale)
                                                                          .header("If-Match", convertToEtag(version)))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Cage with workshopId=%d and cageNumber=%d not found",
                       "Клетка с workshopId=%d и cageNumber=%d не найдена",
                       ROW_ID, CAGE_NUMBER)));

            verify(cageService).delete(ROW_ID, CAGE_NUMBER, version);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.cage.CageControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            mvc.perform(delete(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON).locale(locale))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_wrong_etag() throws Exception {
            mvc.perform(delete(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON).header("If-Match", "bad"))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.cage.CageControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected))
                    .when(cageService).delete(ROW_ID, CAGE_NUMBER, current);

            mvc.perform(delete(API + "/{cageNumber}", ROW_ID, CAGE_NUMBER).accept(JSON)
                                                                          .locale(locale)
                                                                          .header("If-Match", convertToEtag(current)))
               .andExpect(status().isPreconditionFailed());

            verify(cageService).delete(ROW_ID, CAGE_NUMBER, current);
        }
    }

    private CageDTO createDto(Integer id, Integer cageNumber, Integer rowNumber, Long version) {
        return new CageDTO(id, cageNumber, rowNumber, version);
    }

    private CagePostRequest createPostRequest(Integer cageNumber) {
        CagePostRequest request = new CagePostRequest();
        request.setCageNumber(cageNumber);
        return request;
    }

    private CagePutRequest createPutRequest(Integer cageNumber) {
        CagePutRequest request = new CagePutRequest();
        request.setCageNumber(cageNumber);
        return request;
    }

    private CagePatchRequest createPatchRequest(Integer cageNumber) {
        CagePatchRequest request = new CagePatchRequest();
        request.setCageNumber(cageNumber);
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
