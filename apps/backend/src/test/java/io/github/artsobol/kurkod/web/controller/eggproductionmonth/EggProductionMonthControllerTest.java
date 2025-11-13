package io.github.artsobol.kurkod.web.controller.eggproductionmonth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.common.exception.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.error.EggProductionMonthError;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.dto.EggProductionMonthDTO;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPatchRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPostRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.model.request.EggProductionMonthPutRequest;
import io.github.artsobol.kurkod.web.domain.eggproductionmonth.service.api.EggProductionMonthService;
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

@WebMvcTest(controllers = EggProductionMonthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class EggProductionMonthControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/chickens/{chickenId}/egg-productions";
    private static final int CHICKEN_ID = 7;
    private static final int YEAR = 2024;
    private static final int MONTH = 5;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean EggProductionMonthService eggProductionMonthService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    @Nested
    class Create {
        @Test
        @DisplayName("201 Created: body + ETag + Location")
        void returns201_with_payload_etag_and_location() throws Exception {
            EggProductionMonthPostRequest req = createPostRequest(30);
            EggProductionMonthDTO dto = createDto(101, MONTH, YEAR, 30, CHICKEN_ID, 1L);
            when(eggProductionMonthService.create(eq(CHICKEN_ID),
                                                  eq(MONTH),
                                                  eq(YEAR),
                                                  any(EggProductionMonthPostRequest.class))).thenReturn(dto);

            mvc.perform(post(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                              .contentType(JSON)
                                                                              .content(convertToJson(req)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(header().string("Location",
                                          endsWith("/api/v1/chickens/" + CHICKEN_ID + "/egg-productions/" + YEAR + "/" + MONTH)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(101))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(eggProductionMonthService).create(eq(CHICKEN_ID),
                                                     eq(MONTH),
                                                     eq(YEAR),
                                                     any(EggProductionMonthPostRequest.class));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.eggproductionmonth.EggProductionMonthControllerTest#locales")
        @DisplayName("409 Conflict: month already exists for chickenId+year+month")
        void returns409_when_month_already_exists(Locale locale) throws Exception {
            EggProductionMonthPostRequest req = createPostRequest(30);

            when(eggProductionMonthService.create(eq(CHICKEN_ID),
                                                  eq(MONTH),
                                                  eq(YEAR),
                                                  any(EggProductionMonthPostRequest.class)))
                    .thenThrow(new DataExistException(EggProductionMonthError.ALREADY_EXISTS,
                                                      CHICKEN_ID, YEAR, MONTH));

            mvc.perform(post(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH)
                                .accept(JSON)
                                .locale(locale)
                                .contentType(JSON)
                                .content(convertToJson(req)))
               .andExpect(status().isConflict())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Egg production for chickenId=%d, year=%d, month=%d already exists",
                       "Запись о яичной продуктивности уже существует для chickenId=%d, год=%d, месяц=%d",
                       CHICKEN_ID, YEAR, MONTH)));

            verify(eggProductionMonthService).create(eq(CHICKEN_ID),
                                                     eq(MONTH),
                                                     eq(YEAR),
                                                     any(EggProductionMonthPostRequest.class));
        }


        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(eggProductionMonthService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_body_invalid() throws Exception {
            EggProductionMonthPostRequest bad = createPostRequest(-1); // нарушаем @Positive
            mvc.perform(post(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                              .contentType(JSON)
                                                                              .content(convertToJson(bad))).andExpect(
                    status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(eggProductionMonthService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                              .contentType(JSON)
                                                                              .content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(eggProductionMonthService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                              .contentType(MediaType.TEXT_PLAIN)
                                                                              .content("count=30"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(eggProductionMonthService);
        }
    }

    // -------- GET /{year}/{month} --------
    @Nested
    class GetOne {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            EggProductionMonthDTO dto = createDto(101, MONTH, YEAR, 30, CHICKEN_ID, 2L);
            when(eggProductionMonthService.get(CHICKEN_ID, MONTH, YEAR)).thenReturn(dto);

            mvc.perform(get(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(2L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(101))
               .andExpect(jsonPath("$.payload.month").value(MONTH))
               .andExpect(jsonPath("$.payload.year").value(YEAR))
               .andExpect(jsonPath("$.payload.chickenId").value(CHICKEN_ID))
               .andExpect(jsonPath("$.payload.version").value(2));

            verify(eggProductionMonthService).get(CHICKEN_ID, MONTH, YEAR);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource(
                "io.github.artsobol.kurkod.web.controller.eggproductionmonth.EggProductionMonthControllerTest#locales")
        @DisplayName("404 Not Found: not found by keys")
        void returns404_with_i18n_message(Locale locale) throws Exception {
            when(eggProductionMonthService.get(CHICKEN_ID, MONTH, YEAR)).thenThrow(new NotFoundException(
                    EggProductionMonthError.NOT_FOUND_BY_KEYS,
                    CHICKEN_ID,
                    YEAR,
                    MONTH));

            mvc.perform(get(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON).locale(locale)).andExpect(
                    status().isNotFound()).andExpect(content().contentTypeCompatibleWith(JSON)).andExpect(jsonPath(
                    "$.message").value(createMessage(locale,
                                                     "Egg production for chickenId=%d, year=%d, month=%d not found",
                                                     "Яичная продуктивность для chickenId=%d, год=%d, месяц=%d не найдена",
                                                     CHICKEN_ID,
                                                     YEAR,
                                                     MONTH)));

            verify(eggProductionMonthService).get(CHICKEN_ID, MONTH, YEAR);
        }
    }

    // -------- GET (all by chicken) --------
    @Nested
    class GetAllByChicken {
        @Test
        @DisplayName("200 OK: list entities")
        void returns200_with_list() throws Exception {
            List<EggProductionMonthDTO> list = List.of(createDto(1, 1, YEAR, 20, CHICKEN_ID, 1L),
                                                       createDto(2, 2, YEAR, 25, CHICKEN_ID, 1L));
            when(eggProductionMonthService.getAllByChicken(CHICKEN_ID)).thenReturn(list);

            mvc.perform(get(API, CHICKEN_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(2)))
               .andExpect(jsonPath("$.payload[0].month").value(1))
               .andExpect(jsonPath("$.payload[1].month").value(2));

            verify(eggProductionMonthService).getAllByChicken(CHICKEN_ID);
        }

        @Test
        @DisplayName("200 OK: empty list")
        void returns200_with_empty_list() throws Exception {
            when(eggProductionMonthService.getAllByChicken(CHICKEN_ID)).thenReturn(List.of());

            mvc.perform(get(API, CHICKEN_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(eggProductionMonthService).getAllByChicken(CHICKEN_ID);
        }
    }

    // -------- GET /{year} (all by year) --------
    @Nested
    class GetAllByYear {
        @Test
        @DisplayName("200 OK: list entities for year")
        void returns200_with_list_for_year() throws Exception {
            List<EggProductionMonthDTO> list = List.of(createDto(1, 3, YEAR, 22, CHICKEN_ID, 1L),
                                                       createDto(2, 4, YEAR, 26, CHICKEN_ID, 1L));
            when(eggProductionMonthService.getAllByChickenAndYear(CHICKEN_ID, YEAR)).thenReturn(list);

            mvc.perform(get(API + "/{year}", CHICKEN_ID, YEAR).accept(JSON)).andExpect(status().isOk()).andExpect(
                    jsonPath("$.success").value(true)).andExpect(jsonPath("$.payload", hasSize(2))).andExpect(jsonPath(
                    "$.payload[0].year").value(YEAR));

            verify(eggProductionMonthService).getAllByChickenAndYear(CHICKEN_ID, YEAR);
        }
    }

    // -------- PUT /{year}/{month} --------
    @Nested
    class Replace {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long oldV = 5L, newV = 6L;
            EggProductionMonthPutRequest req = createPutRequest(31);
            EggProductionMonthDTO dto = createDto(101, MONTH, YEAR, 31, CHICKEN_ID, newV);
            when(eggProductionMonthService.replace(eq(CHICKEN_ID),
                                                   eq(MONTH),
                                                   eq(YEAR),
                                                   any(EggProductionMonthPutRequest.class),
                                                   eq(oldV))).thenReturn(dto);

            mvc.perform(put(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                             .contentType(JSON)
                                                                             .header("If-Match", convertToEtag(oldV))
                                                                             .content(convertToJson(req))).andExpect(
                    status().isOk()).andExpect(header().string("ETag", convertToEtag(newV))).andExpect(jsonPath(
                    "$.payload.version").value((int) newV));

            verify(eggProductionMonthService).replace(eq(CHICKEN_ID),
                                                      eq(MONTH),
                                                      eq(YEAR),
                                                      any(EggProductionMonthPutRequest.class),
                                                      eq(oldV));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource(
                "io.github.artsobol.kurkod.web.controller.eggproductionmonth.EggProductionMonthControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            EggProductionMonthPutRequest req = createPutRequest(33);

            mvc.perform(put(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                             .locale(locale)
                                                                             .contentType(JSON)
                                                                             .content(convertToJson(req))).andExpect(
                    status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource(
                "io.github.artsobol.kurkod.web.controller.eggproductionmonth.EggProductionMonthControllerTest#locales")
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_invalid_etag(Locale locale) throws Exception {
            EggProductionMonthPutRequest req = createPutRequest(33);

            mvc.perform(put(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON).locale(locale).contentType(
                    JSON).header("If-Match", "trash").content(convertToJson(req))).andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(put(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                             .contentType(JSON)
                                                                             .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(eggProductionMonthService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            EggProductionMonthPutRequest bad = createPutRequest(-10);
            mvc.perform(put(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                             .contentType(JSON)
                                                                             .header("If-Match", convertToEtag(5L))
                                                                             .content(convertToJson(bad))).andExpect(
                    status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(eggProductionMonthService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(put(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                             .contentType(JSON)
                                                                             .header("If-Match", convertToEtag(5L))
                                                                             .content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(eggProductionMonthService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(put(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                             .contentType(MediaType.TEXT_PLAIN)
                                                                             .header("If-Match", convertToEtag(5L))
                                                                             .content("count=31"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(eggProductionMonthService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource(
                "io.github.artsobol.kurkod.web.controller.eggproductionmonth.EggProductionMonthControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(eggProductionMonthService.replace(eq(CHICKEN_ID),
                                                   eq(MONTH),
                                                   eq(YEAR),
                                                   any(EggProductionMonthPutRequest.class),
                                                   eq(current))).thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED,
                                                                                                    current,
                                                                                                    expected));

            EggProductionMonthPutRequest req = createPutRequest(32);
            mvc.perform(put(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON).locale(locale).contentType(
                       JSON).header("If-Match", convertToEtag(current)).content(convertToJson(req)))
               .andExpect(status().isPreconditionFailed());

            verify(eggProductionMonthService).replace(eq(CHICKEN_ID),
                                                      eq(MONTH),
                                                      eq(YEAR),
                                                      any(EggProductionMonthPutRequest.class),
                                                      eq(current));
        }
    }

    // -------- PATCH /{year}/{month} --------
    @Nested
    class Update {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long oldV = 2L, newV = 3L;
            EggProductionMonthPatchRequest req = createPatchRequest(40); // валидно
            EggProductionMonthDTO dto = createDto(101, MONTH, YEAR, 40, CHICKEN_ID, newV);

            when(eggProductionMonthService.update(eq(CHICKEN_ID),
                                                  eq(MONTH),
                                                  eq(YEAR),
                                                  any(EggProductionMonthPatchRequest.class),
                                                  eq(oldV))).thenReturn(dto);

            mvc.perform(patch(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                               .contentType(JSON)
                                                                               .header("If-Match", convertToEtag(oldV))
                                                                               .content(convertToJson(req))).andExpect(
                    status().isOk()).andExpect(header().string("ETag", convertToEtag(newV))).andExpect(jsonPath(
                    "$.payload.version").value((int) newV));

            verify(eggProductionMonthService).update(eq(CHICKEN_ID),
                                                     eq(MONTH),
                                                     eq(YEAR),
                                                     any(EggProductionMonthPatchRequest.class),
                                                     eq(oldV));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                               .contentType(JSON)
                                                                               .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(eggProductionMonthService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            EggProductionMonthPatchRequest bad = createPatchRequest(-100); // нарушаем @Positive
            mvc.perform(patch(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                               .contentType(JSON)
                                                                               .header("If-Match", convertToEtag(5L))
                                                                               .content(convertToJson(bad))).andExpect(
                    status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(eggProductionMonthService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(patch(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                               .contentType(JSON)
                                                                               .header("If-Match", convertToEtag(5L))
                                                                               .content("["))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(eggProductionMonthService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(patch(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                               .contentType(MediaType.TEXT_PLAIN)
                                                                               .header("If-Match", convertToEtag(5L))
                                                                               .content("count=40"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(eggProductionMonthService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource(
                "io.github.artsobol.kurkod.web.controller.eggproductionmonth.EggProductionMonthControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            EggProductionMonthPatchRequest req = createPatchRequest(41);
            mvc.perform(patch(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                               .locale(locale)
                                                                               .contentType(JSON)
                                                                               .content(convertToJson(req))).andExpect(
                    status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource(
                "io.github.artsobol.kurkod.web.controller.eggproductionmonth.EggProductionMonthControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(eggProductionMonthService.update(eq(CHICKEN_ID),
                                                  eq(MONTH),
                                                  eq(YEAR),
                                                  any(EggProductionMonthPatchRequest.class),
                                                  eq(current))).thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED,
                                                                                                   current,
                                                                                                   expected));

            EggProductionMonthPatchRequest req = createPatchRequest(42);
            mvc.perform(patch(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON).locale(locale).contentType(
                       JSON).header("If-Match", convertToEtag(current)).content(convertToJson(req)))
               .andExpect(status().isPreconditionFailed());

            verify(eggProductionMonthService).update(eq(CHICKEN_ID),
                                                     eq(MONTH),
                                                     eq(YEAR),
                                                     any(EggProductionMonthPatchRequest.class),
                                                     eq(current));
        }
    }

    // -------- DELETE /{year}/{month} --------
    @Nested
    class DeleteOne {
        @Test
        @DisplayName("204 No Content: no ETag in response, empty body")
        void returns204_without_etag() throws Exception {
            long v = 5L;
            doNothing().when(eggProductionMonthService).delete(CHICKEN_ID, MONTH, YEAR, v);

            mvc.perform(delete(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                                .header("If-Match", convertToEtag(v)))
               .andExpect(status().isNoContent())
               .andExpect(header().doesNotExist("ETag"))
               .andExpect(content().string(""));

            verify(eggProductionMonthService).delete(CHICKEN_ID, MONTH, YEAR, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource(
                "io.github.artsobol.kurkod.web.controller.eggproductionmonth.EggProductionMonthControllerTest#locales")
        @DisplayName("404 Not Found: not found by keys")
        void returns404_when_not_found(Locale locale) throws Exception {
            long v = 5L;
            doThrow(new NotFoundException(EggProductionMonthError.NOT_FOUND_BY_KEYS, CHICKEN_ID, YEAR, MONTH)).when(
                    eggProductionMonthService).delete(CHICKEN_ID, MONTH, YEAR, v);

            mvc.perform(delete(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                                .locale(locale)
                                                                                .header("If-Match", convertToEtag(v)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(locale,
                                                                    "Egg production for chickenId=%s, year=%s, month=%s not found",
                                                                    "Яичная продуктивность для chickenId=%s, год=%s, месяц=%s не найдена",
                                                                    CHICKEN_ID,
                                                                    YEAR,
                                                                    MONTH)));

            verify(eggProductionMonthService).delete(CHICKEN_ID, MONTH, YEAR, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource(
                "io.github.artsobol.kurkod.web.controller.eggproductionmonth.EggProductionMonthControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            mvc.perform(delete(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON).locale(locale)).andExpect(
                    status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource(
                "io.github.artsobol.kurkod.web.controller.eggproductionmonth.EggProductionMonthControllerTest#locales")
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_wrong_etag(Locale locale) throws Exception {
            mvc.perform(delete(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                                .locale(locale)
                                                                                .header("If-Match", "bad")).andExpect(
                    status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource(
                "io.github.artsobol.kurkod.web.controller.eggproductionmonth.EggProductionMonthControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected)).when(
                    eggProductionMonthService).delete(CHICKEN_ID, MONTH, YEAR, current);

            mvc.perform(delete(API + "/{year}/{month}", CHICKEN_ID, YEAR, MONTH).accept(JSON)
                                                                                .locale(locale)
                                                                                .header("If-Match",
                                                                                        convertToEtag(current)))
               .andExpect(status().isPreconditionFailed());

            verify(eggProductionMonthService).delete(CHICKEN_ID, MONTH, YEAR, current);
        }
    }

    // -------- helpers --------
    private EggProductionMonthDTO createDto(int id, int month, int year, int count, int chickenId, long version) {
        return new EggProductionMonthDTO(id, month, year, count, chickenId, version);
    }

    private EggProductionMonthPostRequest createPostRequest(Integer count) {
        EggProductionMonthPostRequest r = new EggProductionMonthPostRequest();
        r.setCount(count);
        return r;
    }

    private EggProductionMonthPutRequest createPutRequest(Integer count) {
        EggProductionMonthPutRequest r = new EggProductionMonthPutRequest();
        r.setCount(count);
        return r;
    }

    private EggProductionMonthPatchRequest createPatchRequest(Integer count) {
        EggProductionMonthPatchRequest r = new EggProductionMonthPatchRequest();
        r.setCount(count);
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
