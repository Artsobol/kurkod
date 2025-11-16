package io.github.artsobol.kurkod.web.controller.chicken;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.*;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.chicken.error.ChickenError;
import io.github.artsobol.kurkod.web.domain.chicken.model.dto.ChickenDTO;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPatchRequest;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPostRequest;
import io.github.artsobol.kurkod.web.domain.chicken.model.request.ChickenPutRequest;
import io.github.artsobol.kurkod.web.domain.chicken.service.api.ChickenService;
import io.github.artsobol.kurkod.web.domain.common.error.RequiredHeaderError;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ChickenController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({
        CommonControllerAdvice.class,
})
class ChickenControllerTest {

    private static final String API = "/api/v1/chickens";
    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final int ID = 42;
    private static final LocalDate BDAY = LocalDate.of(2020, 1, 1);

    @Autowired MockMvc mvc;

    @Autowired ObjectMapper objectMapper;

    @MockitoBean ChickenService chickenService;

    @MockitoBean JwtTokenProvider jwtTokenProvider;

    @MockitoBean JwtRequestFilter jwtRequestFilter;

    @Nested
    class GetById {
        @Test
        @SneakyThrows
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() {
            ChickenDTO dto = createChickenDto(ID, "Kuka", 543, 1, 1L);
            when(chickenService.get(ID)).thenReturn(dto);

            mvc.perform(get(API + "/{id}", ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", createEtag(1L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(ID))
               .andExpect(jsonPath("$.payload.name").value("Kuka"))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(chickenService).get(ID);
        }

        @ParameterizedTest
        @MethodSource("io.github.artsobol.kurkod.web.controller.chicken.ChickenControllerTest#locales")
        @SneakyThrows
        @DisplayName("404 Not Found: no chicken with this id")
        void returns404_with_i18n_message(Locale locale) {
            when(chickenService.get(ID)).thenThrow(new NotFoundException(ChickenError.NOT_FOUND_BY_ID, ID));


            mvc.perform(get(API + "/{id}", ID).accept(JSON).locale(locale)).andExpect(status().isNotFound()).andExpect(
                    content().contentTypeCompatibleWith(JSON)).andExpect(jsonPath("$.message").value(createMessage(
                    locale,
                    "Chicken with ID=%d not found",
                    "Курица с ID=%d не найдена",
                    ID)));

            verify(chickenService).get(ID);
        }

        @Test
        @SneakyThrows
        @DisplayName("200 OK: list entities")
        void returns200_with_list() {
            ChickenDTO c1 = createChickenDto(1, "Kuka", 543, 1, 3L);
            ChickenDTO c2 = createChickenDto(2, "Buka", 600, 2, 4L);
            when(chickenService.getAll()).thenReturn(List.of(c1, c2));


            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.message").value(""))
               .andExpect(jsonPath("$.payload", hasSize(2)))

               .andExpect(jsonPath("$.payload[0].id").value(1))
               .andExpect(jsonPath("$.payload[0].name").value("Kuka"))
               .andExpect(jsonPath("$.payload[0].weight").value(543))
               .andExpect(jsonPath("$.payload[0].birthDate").value(BDAY.toString()))
               .andExpect(jsonPath("$.payload[0].breedId").value(1))
               .andExpect(jsonPath("$.payload[0].version").value(3))

               .andExpect(jsonPath("$.payload[1].id").value(2))
               .andExpect(jsonPath("$.payload[1].name").value("Buka"))
               .andExpect(jsonPath("$.payload[1].weight").value(600))
               .andExpect(jsonPath("$.payload[1].birthDate").value(BDAY.toString()))
               .andExpect(jsonPath("$.payload[1].breedId").value(2))
               .andExpect(jsonPath("$.payload[1].version").value(4));

            verify(chickenService).getAll();
        }

        @Test
        @SneakyThrows
        @DisplayName("200 OK: empty list")
        void returns200_with_empty_list() {
            when(chickenService.getAll()).thenReturn(List.of());


            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.message").value(""))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(chickenService).getAll();
        }
    }

    @Nested
    class Create {
        @Test
        @SneakyThrows
        @DisplayName("201 Created: body + ETag")
        void returns201_with_payload_and_etag() {
            ChickenPostRequest request = createPostRequest();
            ChickenDTO dto = createChickenDto(1, "Kuka", 543, 1, 1L);

            when(chickenService.create(any())).thenReturn(dto);


            mvc.perform(post(API).accept(JSON)
                                 .contentType(JSON)
                                 .content(convertToJson(request))
                       )
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", createEtag(1L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(1))
               .andExpect(jsonPath("$.payload.name").value("Kuka"));

            verify(chickenService).create(any());
        }

        @Test
        @SneakyThrows
        @DisplayName("400 Bad Request: empty body")
        void returns400_with_when_body_missing() {
            mvc.perform(post(API).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(chickenService);
        }

        @Test
        @SneakyThrows
        @DisplayName("400 Bad Request: invalid body")
        void returns400_with_invalid_body() {
            var bad = new ChickenPostRequest("", -1, BDAY, null);

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(chickenService);
        }

        @Test
        @SneakyThrows
        @DisplayName("400 Bad Request: malformed json")
        void returns400_when_malformed_json() {
            mvc.perform(post(API).accept(JSON).contentType(JSON).content("{")).andExpect(status().isBadRequest());

            verifyNoInteractions(chickenService);
        }

        @Test
        @SneakyThrows
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() {
            mvc.perform(post(API).accept(JSON).contentType(MediaType.TEXT_PLAIN).content("name=Kuka"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(chickenService);
        }

    }

    @Nested
    class Replace {
        @Test
        @SneakyThrows
        @DisplayName("200 OK: body + ETag")
        void returns200_with_new_etag() {
            long oldVersion = 5L, newVersion = 6L;
            var request = createPutRequest("NewName", 700, 2);
            var dto = createChickenDto(ID, "NewName", 700, 2, newVersion);

            when(chickenService.replace(eq(ID), any(ChickenPutRequest.class), eq(oldVersion))).thenReturn(dto);


            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(JSON)
                                              .header("If-Match", createEtag(oldVersion))
                                              .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", createEtag(newVersion)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(ID))
               .andExpect(jsonPath("$.payload.version").value((int) newVersion));

            verify(chickenService).replace(eq(ID), any(ChickenPutRequest.class), eq(oldVersion));
        }

        @ParameterizedTest
        @MethodSource("io.github.artsobol.kurkod.web.controller.chicken.ChickenControllerTest#locales")
        @SneakyThrows
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) {
            ChickenPutRequest request = createPutRequest("Name", 1, 1);

            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .locale(locale)
                                              .contentType(JSON)
                                              .content(convertToJson(request)))
               .andExpect(status().isPreconditionRequired())
               .andExpect(jsonPath("$.message").value(createMessage(locale,
                                                                    "Required header If-Match is missing",
                                                                    "Необходимый заголовок If-Match отсутствует")));
        }

        @ParameterizedTest
        @MethodSource("io.github.artsobol.kurkod.web.controller.chicken.ChickenControllerTest#locales")
        @SneakyThrows
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_invalid_etag(Locale locale) {
            ChickenPutRequest request = createPutRequest("Name", 1, 1);

            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .locale(locale)
                                              .contentType(JSON)
                                              .content(convertToJson(request))
                                              .header("If-Match", "trash"))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.message").value(createMessage(locale,
                                                                    "The If-Match header value is invalid. Provided value: %s",
                                                                    "Значение заголовка If-Match недействительно. Передано значение: %s",
                                                                    "trash")));
        }

        @Test
        @SneakyThrows
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() {
            long v = 5L;

            mvc.perform(put(API + "/{id}", ID).accept(JSON).contentType(JSON).header("If-Match", createEtag(v)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(chickenService);
        }

        @Test
        @SneakyThrows
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() {
            long v = 5L;

            var bad = new ChickenPutRequest("", -1, null, null);

            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(JSON)
                                              .header("If-Match", createEtag(v))
                                              .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(chickenService);
        }

        @Test
        @SneakyThrows
        @DisplayName("400 Bad Request: malformed json")
        void returns400_when_malformed_json() {
            long v = 5L;

            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(JSON)
                                              .header("If-Match", createEtag(v))
                                              .content("{")).andExpect(status().isBadRequest());

            verifyNoInteractions(chickenService);
        }

        @Test
        @SneakyThrows
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() {
            long v = 5L;

            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(MediaType.TEXT_PLAIN)
                                              .header("If-Match", createEtag(v))
                                              .content("name=Kuka")).andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(chickenService);
        }

        @ParameterizedTest
        @MethodSource("io.github.artsobol.kurkod.web.controller.chicken.ChickenControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(chickenService.replace(eq(ID),
                                        any(ChickenPutRequest.class),
                                        eq(current))).thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED,
                                                                                         current,
                                                                                         expected));


            var req = createPutRequest("Nm", 1, 1);
            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .locale(locale)
                                              .contentType(JSON)
                                              .header("If-Match", createEtag(current))
                                              .content(convertToJson(req)))
               .andExpect(status().isPreconditionFailed())
               .andExpect(jsonPath("$.message").value(createMessage(locale,
                                                                    "The resource version does not match the provided one. Current version: %d, provided version: %d",
                                                                    "Версия ресурса не совпадает с переданной. Текущая версия: %d, переданная версия: %d",
                                                                    current,
                                                                    expected)));

            verify(chickenService).replace(eq(ID), any(ChickenPutRequest.class), eq(current));
        }

    }

    @Nested
    class Update {
        @Test
        @SneakyThrows
        @DisplayName("200 OK: body + ETag")
        void returns200_with_new_etag() {
            long oldVersion = 5L;
            long newVersion = 6L;
            ChickenPatchRequest request = createPatchRequest("Kuka");
            ChickenDTO dto = createChickenDto(ID, "Kuka", 700, 2, newVersion);

            when(chickenService.update(eq(ID), any(ChickenPatchRequest.class), eq(oldVersion))).thenReturn(dto);


            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(JSON)
                                                .header("If-Match", createEtag(oldVersion))
                                                .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", createEtag(newVersion)))
               .andExpect(jsonPath("$.payload.version").value((int) newVersion));

            verify(chickenService).update(eq(ID), any(ChickenPatchRequest.class), eq(oldVersion));
        }

        @Test
        @SneakyThrows
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() {
            long v = 5L;

            mvc.perform(patch(API + "/{id}", ID).accept(JSON).contentType(JSON).header("If-Match", createEtag(v)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(chickenService);
        }

        @Test
        @SneakyThrows
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() {
            long v = 5L;
            var bad = ChickenPatchRequest.builder().weight(-1312).build();

            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(JSON)
                                                .header("If-Match", createEtag(v))
                                                .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(chickenService);
        }

        @Test
        @SneakyThrows
        @DisplayName("400 Bad Request: malformed json")
        void returns400_when_malformed_json() {
            long v = 5L;

            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(JSON)
                                                .header("If-Match", createEtag(v))
                                                .content("{")).andExpect(status().isBadRequest());

            verifyNoInteractions(chickenService);
        }

        @Test
        @SneakyThrows
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() {
            long v = 5L;

            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(MediaType.TEXT_PLAIN)
                                                .header("If-Match", createEtag(v))
                                                .content("name=Kuka")).andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(chickenService);
        }

        @ParameterizedTest
        @MethodSource("io.github.artsobol.kurkod.web.controller.chicken.ChickenControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            var req = createPatchRequest("Kuka");
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .locale(locale)
                                                .contentType(JSON)
                                                .content(convertToJson(req)))
               .andExpect(status().isPreconditionRequired())
               .andExpect(jsonPath("$.message").value(createMessage(locale,
                                                                    "Required header If-Match is missing",
                                                                    "Необходимый заголовок If-Match отсутствует")));
        }

        @ParameterizedTest
        @MethodSource("io.github.artsobol.kurkod.web.controller.chicken.ChickenControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(chickenService.update(eq(ID),
                                       any(ChickenPatchRequest.class),
                                       eq(current))).thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED,
                                                                                        current,
                                                                                        expected));

            var req = createPatchRequest("Nm");
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .locale(locale)
                                                .contentType(JSON)
                                                .header("If-Match", createEtag(current))
                                                .content(convertToJson(req)))
               .andExpect(status().isPreconditionFailed());

            verify(chickenService).update(eq(ID), any(ChickenPatchRequest.class), eq(current));
        }

    }

    @Nested
    class Delete {
        @Test
        @SneakyThrows
        @DisplayName("204 No content: echoes ETag")
        void returns204_with_no_body_and_no_etag() {
            long version = 5L;
            doNothing().when(chickenService).delete(ID, version);


            mvc.perform(delete(API + "/{id}", ID).accept(JSON).header("If-Match", createEtag(version)))
               .andExpect(status().isNoContent())
               .andExpect(header().string("ETag", createEtag(version)));

            verify(chickenService).delete(ID, version);
        }

        @ParameterizedTest
        @MethodSource("io.github.artsobol.kurkod.web.controller.chicken.ChickenControllerTest#locales")
        @SneakyThrows
        @DisplayName("404 Not Found: not found")
        void returns404_when_not_found(Locale locale) {
            long version = 5L;
            doThrow(new NotFoundException(ChickenError.NOT_FOUND_BY_ID, ID)).when(chickenService).delete(ID, version);


            mvc.perform(delete(API + "/{id}", ID).accept(JSON).locale(locale).header("If-Match", createEtag(version)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(locale,
                                                                    "Chicken with ID=%d not found",
                                                                    "Курица с ID=%d не найдена",
                                                                    ID)));

            verify(chickenService).delete(ID, version);
        }

        @ParameterizedTest
        @MethodSource("io.github.artsobol.kurkod.web.controller.chicken.ChickenControllerTest#locales")
        @SneakyThrows
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) {
            mvc.perform(delete(API + "/{id}", ID).accept(JSON).locale(locale))
               .andExpect(status().isPreconditionRequired())
               .andExpect(jsonPath("$.message").value(createMessage(locale,
                                                                    "Required header If-Match is missing",
                                                                    "Необходимый заголовок If-Match отсутствует")));
        }

        @ParameterizedTest
        @MethodSource("io.github.artsobol.kurkod.web.controller.chicken.ChickenControllerTest#locales")
        @SneakyThrows
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_wrong_etag(Locale locale) {
            mvc.perform(delete(API + "/{id}", ID).accept(JSON).locale(locale).header("If-Match", "bad")).andExpect(
                    status().isBadRequest()).andExpect(jsonPath("$.message").value(createMessage(locale,
                                                                                                 "The If-Match header value is invalid. Provided value: %s",
                                                                                                 "Значение заголовка If-Match недействительно. Передано значение: %s",
                                                                                                 "bad")));
        }

        @ParameterizedTest
        @MethodSource("io.github.artsobol.kurkod.web.controller.chicken.ChickenControllerTest#locales")
        @SneakyThrows
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) {
            long current = 5L;
            long expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected)).when(chickenService)
                                                                                                  .delete(ID, current);


            mvc.perform(delete(API + "/{id}", ID).accept(JSON).locale(locale).header("If-Match", createEtag(current)))
               .andExpect(status().isPreconditionFailed())
               .andExpect(jsonPath("$.message").value(createMessage(locale,
                                                                    "The resource version does not match the provided one. Current version: %d, provided version: %d",
                                                                    "Версия ресурса не совпадает с переданной. Текущая версия: %d, переданная версия: %d",
                                                                    current,
                                                                    expected)));

            verify(chickenService).delete(ID, current);
        }
    }

    private ChickenDTO createChickenDto(int id, String name, int weight, int breedId, long version) {
        return new ChickenDTO(id, name, weight, BDAY, breedId, version);
    }

    private ChickenPostRequest createPostRequest() {
        return new ChickenPostRequest("Kuka", 543, BDAY, 1);
    }

    private ChickenPutRequest createPutRequest(String name, int weight, int breedId) {
        return new ChickenPutRequest(name, weight, BDAY, breedId);
    }

    private ChickenPatchRequest createPatchRequest(String name) {
        return ChickenPatchRequest.builder().name(name).build();
    }

    private String convertToJson(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }

    private String createEtag(long version) {
        return EtagUtils.toEtag(version);
    }

    public static Stream<Locale> locales() {
        return Stream.of(Locale.ENGLISH, Locale.of("ru", "RU"));
    }

    private static String createMessage(Locale locale, String en, String ru, Object... args) {
        String pattern = locale.getLanguage().equals("ru") ? ru : en;
        return args.length == 0 ? pattern : String.format(pattern, args);
    }

}