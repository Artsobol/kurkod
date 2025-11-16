package io.github.artsobol.kurkod.web.controller.breed;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.common.error.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.breed.error.BreedError;
import io.github.artsobol.kurkod.web.domain.breed.model.dto.BreedDTO;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPatchRequest;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPostRequest;
import io.github.artsobol.kurkod.web.domain.breed.model.request.BreedPutRequest;
import io.github.artsobol.kurkod.web.domain.breed.service.api.BreedService;
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

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

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

@WebMvcTest(controllers = BreedController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class BreedControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/breeds";
    private static final int ID = 42;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean BreedService breedService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;
    
    @Nested
    class GetById {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            BreedDTO dto = createDto(ID, "Leghorn", 200, 2500, 1L);
            when(breedService.get(ID)).thenReturn(dto);

            mvc.perform(get(API + "/{id}", ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(ID))
               .andExpect(jsonPath("$.payload.name").value("Leghorn"))
               .andExpect(jsonPath("$.payload.eggsNumber").value(200))
               .andExpect(jsonPath("$.payload.weight").value(2500))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(breedService).get(ID);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.breed.BreedControllerTest#locales")
        @DisplayName("404 Not Found: no breed with this id")
        void returns404_with_i18n_message(Locale locale) throws Exception {
            when(breedService.get(ID)).thenThrow(new NotFoundException(BreedError.NOT_FOUND_BY_ID, ID));

            mvc.perform(get(API + "/{id}", ID).accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(locale,
                                                          "Breed with ID=%d not found",
                                                          "Порода с ID=%d не найдена",
                                                          ID)));

            verify(breedService).get(ID);
        }

        @Test
        @DisplayName("200 OK: list entities")
        void returns200_with_list() throws Exception {
            List<BreedDTO> list = List.of(createDto(1, "Leghorn", 200, 2500, 3L), createDto(2, "Rhode Island", 180, 3000, 4L));
            when(breedService.getAll()).thenReturn(list);

            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(2)))
               .andExpect(jsonPath("$.payload[0].id").value(1))
               .andExpect(jsonPath("$.payload[0].name").value("Leghorn"))
               .andExpect(jsonPath("$.payload[0].eggsNumber").value(200))
               .andExpect(jsonPath("$.payload[0].weight").value(2500))
               .andExpect(jsonPath("$.payload[0].version").value(3))
               .andExpect(jsonPath("$.payload[1].id").value(2))
               .andExpect(jsonPath("$.payload[1].name").value("Rhode Island"))
               .andExpect(jsonPath("$.payload[1].eggsNumber").value(180))
               .andExpect(jsonPath("$.payload[1].weight").value(3000))
               .andExpect(jsonPath("$.payload[1].version").value(4));

            verify(breedService).getAll();
        }

        @Test
        @DisplayName("200 OK: empty list")
        void returns200_with_empty_list() throws Exception {
            when(breedService.getAll()).thenReturn(List.of());

            mvc.perform(get(API).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(breedService).getAll();
        }
    }
    
    @Nested
    class Create {
        @Test
        @DisplayName("201 Created: body + ETag + Location")
        void returns201_with_payload_etag_and_location() throws Exception {
            BreedPostRequest request = createPostRequest();
            BreedDTO dto = createDto(1, "Leghorn", 200, 2500, 1L);
            when(breedService.create(any(BreedPostRequest.class))).thenReturn(dto);

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(request)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(header().string("Location",
                                          org.hamcrest.Matchers.endsWith(API + "/1")))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(1))
               .andExpect(jsonPath("$.payload.name").value("Leghorn"));

            verify(breedService).create(any(BreedPostRequest.class));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.breed.BreedControllerTest#locales")
        @SneakyThrows
        @DisplayName("409 Conflict: breed with this name already exist")
        void returns409_when_breed_already_exist(Locale locale){
            BreedPostRequest request = createPostRequest();
            when(breedService.create(request)).thenThrow(new DataExistException(BreedError.ALREADY_EXISTS, request.getName()));

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(request)).locale(locale))
                    .andExpect(status().isConflict())
                    .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(locale,
                                                          "Breed with name=%s already exists",
                                                          "Порода с именем=%s уже существует",
                                                          request.getName())));

            verify(breedService).create(request);
        }


        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(breedService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_body_invalid() throws Exception {
            BreedPostRequest bad = new BreedPostRequest();
            mvc.perform(post(API).accept(JSON).contentType(JSON).content(convertToJson(bad))).andExpect(
                    status().isBadRequest()).andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(breedService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed convertToJson")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON).content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(breedService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API).accept(JSON)
                                 .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                                 .content("name=Leghorn")).andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(breedService);
        }
    }

    @Nested
    class Replace {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_new_etag() throws Exception {
            long oldV = 5L;
            long newV = 6L;
            BreedPutRequest request = createPutRequest("NewName", 220, 2700);
            BreedDTO dto = createDto(ID, "NewName", 220, 2700, newV);
            when(breedService.replace(eq(ID), any(BreedPutRequest.class), eq(oldV))).thenReturn(dto);

            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(JSON)
                                              .header("If-Match", convertToEtag(oldV))
                                              .content(convertToJson(request))).andExpect(status().isOk()).andExpect(header().string(
                    "ETag",
                    convertToEtag(newV))).andExpect(jsonPath("$.success").value(true)).andExpect(jsonPath("$.payload.id").value(
                    ID)).andExpect(jsonPath("$.payload.version").value((int) newV));

            verify(breedService).replace(eq(ID), any(BreedPutRequest.class), eq(oldV));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.breed.BreedControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            BreedPutRequest request = createPutRequest("Name", 1, 1);

            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .locale(locale)
                                              .contentType(JSON)
                                              .content(convertToJson(request))).andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.breed.BreedControllerTest#locales")
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_invalid_etag(Locale locale) throws Exception {
            BreedPutRequest request = createPutRequest("Name", 1, 1);

            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .locale(locale)
                                              .contentType(JSON)
                                              .header("If-Match", "trash")
                                              .content(convertToJson(request))).andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(JSON)
                                              .header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(breedService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            BreedPutRequest bad = new BreedPutRequest(null, null, null);
            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(JSON)
                                              .header("If-Match", convertToEtag(5L))
                                              .content(convertToJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(breedService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed convertToJson")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(JSON)
                                              .header("If-Match", convertToEtag(5L))
                                              .content("{")).andExpect(status().isBadRequest());

            verifyNoInteractions(breedService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                                              .header("If-Match", convertToEtag(5L))
                                              .content("name=New")).andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(breedService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.breed.BreedControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L;
            long expected = 10L;
            when(breedService.replace(eq(ID),
                                      any(BreedPutRequest.class),
                                      eq(current))).thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED,
                                                                                       current,
                                                                                       expected));

            BreedPutRequest request = createPutRequest("Nm", 1, 1);
            mvc.perform(put(API + "/{id}", ID).accept(JSON)
                                              .locale(locale)
                                              .contentType(JSON)
                                              .header("If-Match", convertToEtag(current))
                                              .content(convertToJson(request))).andExpect(status().isPreconditionFailed());

            verify(breedService).replace(eq(ID), any(BreedPutRequest.class), eq(current));
        }
    }

    @Nested
    class Update {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_new_etag() throws Exception {
            long oldV = 5L;
            long newV = 6L;
            BreedPatchRequest request = createPatchRequest("N");
            BreedDTO dto = createDto(ID, "N", 230, 2800, newV);
            when(breedService.update(eq(ID), any(BreedPatchRequest.class), eq(oldV))).thenReturn(dto);

            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(JSON)
                                                .header("If-Match", convertToEtag(oldV))
                                                .content(convertToJson(request)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(newV)))
               .andExpect(jsonPath("$.payload.version").value((int) newV));

            verify(breedService).update(eq(ID), any(BreedPatchRequest.class), eq(oldV));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(JSON)
                                                .header("If-Match", convertToEtag(5L))).andExpect(status().isBadRequest());

            verifyNoInteractions(breedService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            BreedPatchRequest bad = BreedPatchRequest.builder().eggsNumber(-1).build();
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(JSON)
                                                .header("If-Match", convertToEtag(5L))
                                                .content(convertToJson(bad))).andExpect(status().isBadRequest()).andExpect(
                    content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(breedService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed convertToJson")
        void returns400_when_malformed_json() throws Exception{
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(JSON)
                                                .header("If-Match", convertToEtag(5L))
                                                .content("[")).andExpect(status().isBadRequest());

            verifyNoInteractions(breedService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: wrong content type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                                                .header("If-Match", convertToEtag(5L))
                                                .content("name=N")).andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(breedService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.breed.BreedControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            BreedPatchRequest request = createPatchRequest("N");
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .locale(locale)
                                                .contentType(JSON)
                                                .content(convertToJson(request))).andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.breed.BreedControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L;
            long expected = 10L;
            when(breedService.update(eq(ID),
                                     any(BreedPatchRequest.class),
                                     eq(current))).thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED,
                                                                                      current,
                                                                                      expected));

            BreedPatchRequest request = createPatchRequest("Nm");
            mvc.perform(patch(API + "/{id}", ID).accept(JSON)
                                                .locale(locale)
                                                .contentType(JSON)
                                                .header("If-Match", convertToEtag(current))
                                                .content(convertToJson(request))).andExpect(status().isPreconditionFailed());

            verify(breedService).update(eq(ID), any(BreedPatchRequest.class), eq(current));
        }
    }
    
    @Nested
    class Delete {
        @Test
        @DisplayName("204 No Content: no ETag in response")
        void returns204_without_etag() throws Exception {
            long v = 5L;
            doNothing().when(breedService).delete(ID, v);

            mvc.perform(delete(API + "/{id}", ID).accept(JSON).header("If-Match", convertToEtag(v)))
               .andExpect(status().isNoContent())
               .andExpect(header().doesNotExist("ETag"));

            verify(breedService).delete(ID, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.breed.BreedControllerTest#locales")
        @DisplayName("404 Not Found: not found")
        void returns404_when_not_found(Locale locale) throws Exception {
            long v = 5L;
            doThrow(new NotFoundException(BreedError.NOT_FOUND_BY_ID, ID)).when(breedService).delete(ID, v);

            mvc.perform(delete(API + "/{id}", ID).accept(JSON).locale(locale).header("If-Match", convertToEtag(v)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(locale,
                                                          "Breed with ID=%s not found",
                                                          "Порода с ID=%s не найдена",
                                                          ID)));

            verify(breedService).delete(ID, v);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.breed.BreedControllerTest#locales")
        @DisplayName("428 Precondition Required: no If-Match header")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            mvc.perform(delete(API + "/{id}", ID).accept(JSON).locale(locale))
               .andExpect(status().isPreconditionRequired());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.breed.BreedControllerTest#locales")
        @DisplayName("400 Bad Request: invalid If-Match header value")
        void returns400_when_wrong_etag(Locale locale) throws Exception {
            mvc.perform(delete(API + "/{id}", ID).accept(JSON).locale(locale).header("If-Match", "bad"))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.breed.BreedControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L;
            long expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected)).when(breedService)
                                                                                                  .delete(ID, current);

            mvc.perform(delete(API + "/{id}", ID).accept(JSON)
                                                 .locale(locale)
                                                 .header("If-Match", convertToEtag(current)))
               .andExpect(status().isPreconditionFailed());

            verify(breedService).delete(ID, current);
        }
    }
    
    private BreedDTO createDto(int id, String name, int eggs, int weight, long version) {
        return new BreedDTO(id, name, eggs, weight, version);
    }

    private BreedPostRequest createPostRequest() {
        return new BreedPostRequest("Leghorn", 200, 2500);
    }

    private BreedPutRequest createPutRequest(String name, int eggs, int weight) {
        return new BreedPutRequest(name, eggs, weight);
    }

    private BreedPatchRequest createPatchRequest(String name) {
        return BreedPatchRequest.builder().name(name).build();
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
