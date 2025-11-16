package io.github.artsobol.kurkod.web.controller.diet;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.common.error.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.common.model.Season;
import io.github.artsobol.kurkod.web.domain.diet.model.dto.DietDTO;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPatchRequest;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPostRequest;
import io.github.artsobol.kurkod.web.domain.diet.model.request.DietPutRequest;
import io.github.artsobol.kurkod.web.domain.diet.service.api.DietService;
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

@WebMvcTest(controllers = DietController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class DietControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/diets";
    private static final int ID = 42;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean DietService dietService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    // ---------- GET /{id} ----------
    @Nested
    class GetById {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            DietDTO dto = createDto(ID, "Winter Basic", "WIN-B", "desc", Season.WINTER, 1L);
            when(dietService.get(ID)).thenReturn(dto);

            mvc.perform(get(API + "/{id}", ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(ID))
               .andExpect(jsonPath("$.payload.title").value("Winter Basic"))
               .andExpect(jsonPath("$.payload.code").value("WIN-B"))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(dietService).get(ID);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.diet.DietControllerTest#locales")
        @DisplayName("404 Not Found: no diet with this id")
        void returns404_with_i18n_message(Locale locale) throws Exception {
            when(dietService.get(ID)).thenThrow(new NotFoundException(
                    io.github.artsobol.kurkod.web.domain.diet.error.DietError.NOT_FOUND_BY_ID, ID));

            mvc.perform(get(API + "/{id}", ID).accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Diet with ID=%d not found",
                       "Рацион с ID=%d не найден",
                       ID)));

            verify(dietService).get(ID);
        }
    }

    // ---------- GET ----------
    @Nested
    class GetAll {
        @Test
        @DisplayName("200 OK: list entities")
        void returns200_with_list() throws Exception {
            List<DietDTO> list = List.of(
                    createDto(1, "A", "A1", null, Season.SUMMER, 2L),
                    createDto(2, "B", "B1", null, Season.WINTER, 3L)
                                        );
            when(dietService.getAll()).thenReturn(list);

            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(2)))
               .andExpect(jsonPath("$.payload[0].id").value(1))
               .andExpect(jsonPath("$.payload[0].title").value("A"))
               .andExpect(jsonPath("$.payload[1].id").value(2))
               .andExpect(jsonPath("$.payload[1].title").value("B"));

            verify(dietService).getAll();
        }

        @Test
        @DisplayName("200 OK: empty list")
        void returns200_with_empty_list() throws Exception {
            when(dietService.getAll()).thenReturn(List.of());

            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(dietService).getAll();
        }
    }

    // ---------- POST ----------
    @Nested
    class Create {
        @Test
        @DisplayName("201 Created: body + ETag + Location")
        void returns201_with_payload_etag_and_location() throws Exception {
            DietPostRequest request = createPostRequest("Winter Basic", "WIN-B", "desc", Season.WINTER, List.of(1,2));
            DietDTO dto = createDto(7, "Winter Basic", "WIN-B", "desc", Season.WINTER, 5L);
            when(dietService.create(any(DietPostRequest.class))).thenReturn(dto);

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(5L)))
               .andExpect(header().string("Location", endsWith(API + "/7")))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(7))
               .andExpect(jsonPath("$.payload.version").value(5));

            verify(dietService).create(any(DietPostRequest.class));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.diet.DietControllerTest#locales")
        @DisplayName("409 Conflict: diet already exists (title/code)")
        void returns409_when_diet_already_exist(Locale locale) throws Exception {
            DietPostRequest request = createPostRequest("Winter Basic", "WIN-B", "desc", Season.WINTER, List.of(1,2));
            when(dietService.create(any(DietPostRequest.class))).thenThrow(new DataExistException(
                    io.github.artsobol.kurkod.web.domain.diet.error.DietError.ALREADY_EXISTS, request.getTitle()));

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(request)).locale(locale))
               .andExpect(status().isConflict())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Diet already exists: %s",
                       "Рацион уже существует: %s",
                       request.getTitle())));

            verify(dietService).create(any(DietPostRequest.class));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(dietService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_body_invalid() throws Exception {
            DietPostRequest bad = new DietPostRequest(); // пустые поля
            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(dietService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON).content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(dietService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API).accept(JSON)
                                 .contentType(MediaType.TEXT_PLAIN)
                                 .content("title=Win&code=W"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(dietService);
        }
    }

    // ---------- PUT /{id} ----------
    @Nested
    class Replace {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long oldV = 5L, newV = 6L;
            DietPutRequest request = createPutRequest("X1", "X1", "desc", Season.SUMMER, List.of(1,3));
            DietDTO dto = createDto(ID, "X", "X1", "desc", Season.SUMMER, newV);
            when(dietService.replace(eq(ID), any(DietPutRequest.class), eq(oldV))).thenReturn(dto);

            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(JSON)
                                              .header("If-Match", convertToEtag(oldV))
                                              .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(newV)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(ID))
               .andExpect(jsonPath("$.payload.version").value((int) newV));

            verify(dietService).replace(eq(ID), any(DietPutRequest.class), eq(oldV));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.diet.DietControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            DietPutRequest request = createPutRequest("X2", "X1", "desc", Season.SUMMER, List.of(1));

            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .locale(locale)
                                              .contentType(JSON)
                                              .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.diet.DietControllerTest#locales")
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_invalid_etag(Locale locale) throws Exception {
            DietPutRequest request = createPutRequest("X", "X1", "desc", Season.SUMMER, List.of(1));

            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .locale(locale)
                                              .contentType(JSON)
                                              .header("If-Match", "trash")
                                              .content(convertToJson(request)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(JSON)
                                              .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(dietService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            DietPutRequest bad = new DietPutRequest(); // пустые поля
            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(JSON)
                                              .header("If-Match", convertToEtag(5L))
                                              .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(dietService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(JSON)
                                              .header("If-Match", convertToEtag(5L))
                                              .content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(dietService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(MediaType.TEXT_PLAIN)
                                              .header("If-Match", convertToEtag(5L))
                                              .content("title=X"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(dietService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.diet.DietControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(dietService.replace(eq(ID), any(DietPutRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            DietPutRequest request = createPutRequest("X1", "X1", "desc", Season.SUMMER, List.of(1));
            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .locale(locale)
                                              .contentType(JSON)
                                              .header("If-Match", convertToEtag(current))
                                              .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(dietService).replace(eq(ID), any(DietPutRequest.class), eq(current));
        }
    }

    // ---------- PATCH /{id} ----------
    @Nested
    class Update {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long oldV = 5L, newV = 6L;
            DietPatchRequest request = createPatchRequest("Y1", "Y1", "desc", Season.WINTER, List.of(3), List.of(1));
            DietDTO dto = createDto(ID, "Y", "Y1", "desc", Season.WINTER, newV);
            when(dietService.update(eq(ID), any(DietPatchRequest.class), eq(oldV))).thenReturn(dto);

            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(JSON)
                                                .header("If-Match", convertToEtag(oldV))
                                                .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(newV)))
               .andExpect(jsonPath("$.payload.version").value((int) newV));

            verify(dietService).update(eq(ID), any(DietPatchRequest.class), eq(oldV));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(JSON)
                                                .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(dietService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            // нарушаем минимальные размеры
            DietPatchRequest bad = createPatchRequest("Z", "1", null, null, null, null);
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(JSON)
                                                .header("If-Match", convertToEtag(5L))
                                                .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(dietService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception{
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(JSON)
                                                .header("If-Match", convertToEtag(5L))
                                                .content("["))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(dietService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(MediaType.TEXT_PLAIN)
                                                .header("If-Match", convertToEtag(5L))
                                                .content("title=Y"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(dietService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.diet.DietControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            DietPatchRequest request = createPatchRequest("Y1", "Y1", null, null, null, null);
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .locale(locale)
                                                .contentType(JSON)
                                                .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.diet.DietControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(dietService.update(eq(ID), any(DietPatchRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            DietPatchRequest request = createPatchRequest("Y3", "Y1", null, null, null, null);
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .locale(locale)
                                                .contentType(JSON)
                                                .header("If-Match", convertToEtag(current))
                                                .content(convertToJson(request)))
               .andExpect(status().isPreconditionFailed());

            verify(dietService).update(eq(ID), any(DietPatchRequest.class), eq(current));
        }
    }

    // ---------- DELETE /{id} ----------
    @Nested
    class Delete {
        @Test
        @DisplayName("204 No Content: no ETag in response, empty body")
        void returns204_without_etag() throws Exception {
            long v = 5L;
            doNothing().when(dietService).delete(ID, v);

            mvc.perform(delete(API + "/{id}", ID).accept(JSON).header("If-Match", convertToEtag(v)))
               .andExpect(status().isNoContent())
               .andExpect(header().doesNotExist("ETag"))
               .andExpect(content().string(""));

            verify(dietService).delete(ID, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.diet.DietControllerTest#locales")
        @DisplayName("404 Not Found: not found")
        void returns404_when_not_found(Locale locale) throws Exception {
            long v = 5L;
            doThrow(new NotFoundException(
                    io.github.artsobol.kurkod.web.domain.diet.error.DietError.NOT_FOUND_BY_ID, ID))
                    .when(dietService).delete(ID, v);

            mvc.perform(delete(API + "/{id}", ID).accept(JSON).locale(locale).header("If-Match", convertToEtag(v)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Diet with ID=%s not found",
                       "Рацион с ID=%s не найден",
                       ID)));

            verify(dietService).delete(ID, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.diet.DietControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            mvc.perform(delete(API + "/{id}", ID).accept(JSON).locale(locale))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.diet.DietControllerTest#locales")
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_wrong_etag(Locale locale) throws Exception {
            mvc.perform(delete(API + "/{id}", ID).accept(JSON).locale(locale).header("If-Match", "bad"))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.diet.DietControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected))
                    .when(dietService).delete(ID, current);

            mvc.perform(delete(API + "/{id}", ID).accept(JSON)
                                                 .locale(locale)
                                                 .header("If-Match", convertToEtag(current)))
               .andExpect(status().isPreconditionFailed());

            verify(dietService).delete(ID, current);
        }
    }

    // -------- helpers --------
    private DietDTO createDto(int id, String title, String code, String desc, Season season, long version) {
        return new DietDTO(id, title, code, desc, season, version);
    }

    private DietPostRequest createPostRequest(String title, String code, String desc, Season season, List<Integer> breedIds) {
        DietPostRequest r = new DietPostRequest();
        r.setTitle(title);
        r.setCode(code);
        r.setDescription(desc);
        r.setSeason(season);
        r.setBreedIds(breedIds == null ? null : new java.util.HashSet<>(breedIds));
        return r;
    }

    private DietPutRequest createPutRequest(String title, String code, String desc, Season season, List<Integer> breedIds) {
        DietPutRequest r = new DietPutRequest();
        r.setTitle(title);
        r.setCode(code);
        r.setDescription(desc);
        r.setSeason(season);
        r.setBreedIds(breedIds == null ? null : new java.util.HashSet<>(breedIds));
        return r;
    }

    private DietPatchRequest createPatchRequest(String title, String code, String desc, Season season,
                                                List<Integer> add, List<Integer> remove) {
        DietPatchRequest r = new DietPatchRequest();
        r.setTitle(title);
        r.setCode(code);
        r.setDescription(desc);
        r.setSeason(season);
        if (add != null) r.setAddBreedsIds(new java.util.HashSet<>(add));
        if (remove != null) r.setRemoveBreedsIds(new java.util.HashSet<>(remove));
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
