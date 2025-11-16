package io.github.artsobol.kurkod.web.controller.iam;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.common.exception.DataExistException;
import io.github.artsobol.kurkod.common.exception.MatchFailedException;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.common.error.RequiredHeaderError;
import io.github.artsobol.kurkod.common.util.EtagUtils;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.iam.role.model.dto.RoleDTO;
import io.github.artsobol.kurkod.web.domain.iam.user.error.UserError;
import io.github.artsobol.kurkod.web.domain.iam.user.model.dto.UserDTO;
import io.github.artsobol.kurkod.web.domain.iam.user.model.enums.RegistrationStatus;
import io.github.artsobol.kurkod.web.domain.iam.user.model.request.UserPatchRequest;
import io.github.artsobol.kurkod.web.domain.iam.user.model.request.UserPostRequest;
import io.github.artsobol.kurkod.web.domain.iam.user.model.request.UserPutRequest;
import io.github.artsobol.kurkod.web.domain.iam.user.service.api.UserService;
import io.github.artsobol.kurkod.web.domain.iam.userrole.model.entity.UserRole;
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

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class UserControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;
    private static final String API = "/api/v1/users";
    private static final int USER_ID = 5;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean UserService userService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    // ---------- GET BY ID ----------
    @Nested
    class GetById {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            UserDTO dto = createUserDto(USER_ID, "john", "john@example.com", 3L);
            when(userService.getById(USER_ID)).thenReturn(dto);

            mvc.perform(get(API + "/id/{userId}", USER_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(3L)))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(USER_ID))
               .andExpect(jsonPath("$.payload.username").value("john"))
               .andExpect(jsonPath("$.payload.version").value(3));

            verify(userService).getById(USER_ID);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("404 Not Found")
        void returns404(Locale locale) throws Exception {
            when(userService.getById(USER_ID)).thenThrow(new NotFoundException(UserError.NOT_FOUND_BY_ID, USER_ID));

            mvc.perform(get(API + "/id/{userId}", USER_ID).accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "User with ID=%d not found",
                       "Пользователь с ID=%d не найден",
                       USER_ID)));
        }
    }

    // ---------- GET BY USERNAME ----------
    @Nested
    class GetByUsername {
        @Test
        @DisplayName("200 OK: body + ETag")
        void returns200_with_payload_and_etag() throws Exception {
            String username = "john";
            UserDTO dto = createUserDto(USER_ID, username, "john@example.com", 2L);
            when(userService.getByUsername(username)).thenReturn(dto);

            mvc.perform(get(API + "/username/{username}", username).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(2L)))
               .andExpect(jsonPath("$.payload.username").value("john"))
               .andExpect(jsonPath("$.payload.version").value(2));

            verify(userService).getByUsername(username);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("404 Not Found")
        void returns404(Locale locale) throws Exception {
            String username = "ghost";
            when(userService.getByUsername(username))
                    .thenThrow(new NotFoundException(UserError.NOT_FOUND_BY_USERNAME, username));

            mvc.perform(get(API + "/username/{username}", username).accept(JSON).locale(locale))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "User with username=%s not found",
                       "Пользователь с именем=%s не найден",
                       username)));
        }
    }

    // ---------- CREATE ----------
    @Nested
    class CreateOne {
        @Test
        @DisplayName("201 Created: body + ETag + Location")
        void returns201_with_etag_and_location() throws Exception {
            UserPostRequest req = new UserPostRequest("john", "secret", "john@example.com");
            UserDTO dto = createUserDto(101, "john", "john@example.com", 1L);
            when(userService.create(any(UserPostRequest.class))).thenReturn(dto);

            mvc.perform(post(API).accept(JSON).contentType(JSON).content(asJson(req)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("ETag", convertToEtag(1L)))
               .andExpect(header().string("Location", endsWith("/api/v1/users/id/101")))
               .andExpect(jsonPath("$.payload.id").value(101))
               .andExpect(jsonPath("$.payload.username").value("john"))
               .andExpect(jsonPath("$.payload.version").value(1));

            verify(userService).create(any(UserPostRequest.class));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("409 Conflict: username already exists")
        void returns409(Locale locale) throws Exception {
            UserPostRequest req = new UserPostRequest("john","secret","john@example.com");
            when(userService.create(any(UserPostRequest.class)))
                    .thenThrow(new DataExistException(UserError.WITH_USERNAME_ALREADY_EXISTS, "john"));

            mvc.perform(post(API).accept(JSON).locale(locale).contentType(JSON).content(asJson(req)))
               .andExpect(status().isConflict())
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "Username %s already exists",
                       "Имя пользователя %s уже занято",
                       "john")));
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_body_missing() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(userService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            UserPostRequest bad = new UserPostRequest("", null, "bad");
            mvc.perform(post(API).accept(JSON).contentType(JSON).content(asJson(bad)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
        }

        @Test
        @DisplayName("400 Bad Request: malformed JSON")
        void returns400_when_malformed_json() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(JSON).content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type")
        void returns415_when_wrong_content_type() throws Exception {
            mvc.perform(post(API).accept(JSON).contentType(MediaType.TEXT_PLAIN).content("x=y"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(userService);
        }
    }

    // ---------- REPLACE ----------
    @Nested
    class ReplaceOne {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long current = 5L, next = 6L;
            UserPutRequest req = new UserPutRequest("johnny","secret2","johnny@example.com");
            UserDTO dto = createUserDto(USER_ID, "johnny", "johnny@example.com", next);
            when(userService.replace(eq(USER_ID), any(UserPutRequest.class), eq(current))).thenReturn(dto);

            mvc.perform(put(API + "/{userId}", USER_ID).accept(JSON)
                                                       .contentType(JSON)
                                                       .header("If-Match", convertToEtag(current))
                                                       .content(asJson(req)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(next)))
               .andExpect(jsonPath("$.payload.version").value((int) next));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428_when_etag_missing(Locale locale) throws Exception {
            UserPutRequest req = new UserPutRequest("johnny","secret2","johnny@example.com");

            mvc.perform(put(API + "/{userId}", USER_ID).accept(JSON).locale(locale).contentType(JSON).content(asJson(req)))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            UserPutRequest req = new UserPutRequest("johnny","secret2","johnny@example.com");
            mvc.perform(put(API + "/{userId}", USER_ID).accept(JSON).contentType(JSON).header("If-Match","trash").content(asJson(req)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(put(API + "/{userId}", USER_ID).accept(JSON).contentType(JSON).header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            UserPutRequest bad = new UserPutRequest("", null, "bad");
            mvc.perform(put(API + "/{userId}", USER_ID).accept(JSON).contentType(JSON).header("If-Match", convertToEtag(5L)).content(asJson(bad)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412_when_etag_mismatch(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(userService.replace(eq(USER_ID), any(UserPutRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            UserPutRequest req = new UserPutRequest("johnny","secret2","johnny@example.com");
            mvc.perform(put(API + "/{userId}", USER_ID).accept(JSON).locale(locale).contentType(JSON)
                                                       .header("If-Match", convertToEtag(current)).content(asJson(req)))
               .andExpect(status().isPreconditionFailed());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("404 Not Found")
        void returns404(Locale locale) throws Exception {
            long current = 5L;
            when(userService.replace(eq(USER_ID), any(UserPutRequest.class), eq(current)))
                    .thenThrow(new NotFoundException(UserError.NOT_FOUND_BY_ID, USER_ID));

            UserPutRequest req = new UserPutRequest("johnny","secret2","johnny@example.com");
            mvc.perform(put(API + "/{userId}", USER_ID).accept(JSON).locale(locale).contentType(JSON)
                                                       .header("If-Match", convertToEtag(current)).content(asJson(req)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "User with ID=%s not found",
                       "Пользователь с ID=%s не найден",
                       USER_ID)));
        }
    }

    // ---------- PATCH ----------
    @Nested
    class UpdatePartially {
        @Test
        @DisplayName("200 OK: body + new ETag")
        void returns200_with_new_etag() throws Exception {
            long current = 7L, next = 8L;
            UserPatchRequest req = new UserPatchRequest("johnny", null, null);
            UserDTO dto = createUserDto(USER_ID, "johnny", "john@example.com", next);
            when(userService.update(eq(USER_ID), any(UserPatchRequest.class), eq(current))).thenReturn(dto);

            mvc.perform(patch(API + "/{userId}", USER_ID).accept(JSON).contentType(JSON)
                                                         .header("If-Match", convertToEtag(current)).content(asJson(req)))
               .andExpect(status().isOk())
               .andExpect(header().string("ETag", convertToEtag(next)))
               .andExpect(jsonPath("$.payload.version").value((int) next));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428(Locale locale) throws Exception {
            UserPatchRequest req = new UserPatchRequest("johnny", null, null);

            mvc.perform(patch(API + "/{userId}", USER_ID).accept(JSON).locale(locale).contentType(JSON).content(asJson(req)))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_invalid_etag() throws Exception {
            UserPatchRequest req = new UserPatchRequest("johnny", null, null);
            mvc.perform(patch(API + "/{userId}", USER_ID).accept(JSON).contentType(JSON).header("If-Match","bad").content(asJson(req)))
               .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("400 Bad Request: empty body")
        void returns400_when_empty_body() throws Exception {
            mvc.perform(patch(API + "/{userId}", USER_ID).accept(JSON).contentType(JSON).header("If-Match", convertToEtag(5L)))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(userService);
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_when_invalid_body() throws Exception {
            UserPatchRequest bad = new UserPatchRequest("x".repeat(100), "p", "bad");
            mvc.perform(patch(API + "/{userId}", USER_ID).accept(JSON).contentType(JSON).header("If-Match", convertToEtag(5L)).content(asJson(bad)))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            when(userService.update(eq(USER_ID), any(UserPatchRequest.class), eq(current)))
                    .thenThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected));

            UserPatchRequest req = new UserPatchRequest("johnny", null, null);
            mvc.perform(patch(API + "/{userId}", USER_ID).accept(JSON).locale(locale).contentType(JSON)
                                                         .header("If-Match", convertToEtag(current)).content(asJson(req)))
               .andExpect(status().isPreconditionFailed());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("404 Not Found")
        void returns404(Locale locale) throws Exception {
            long current = 5L;
            when(userService.update(eq(USER_ID), any(UserPatchRequest.class), eq(current)))
                    .thenThrow(new NotFoundException(UserError.NOT_FOUND_BY_ID, USER_ID));

            UserPatchRequest req = new UserPatchRequest("johnny", null, null);
            mvc.perform(patch(API + "/{userId}", USER_ID).accept(JSON).locale(locale).contentType(JSON)
                                                         .header("If-Match", convertToEtag(current)).content(asJson(req)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "User with ID=%s not found",
                       "Пользователь с ID=%s не найден",
                       USER_ID)));
        }
    }

    // ---------- DELETE ----------
    @Nested
    class DeleteOne {
        @Test
        @DisplayName("204 No Content")
        void returns204() throws Exception {
            long v = 9L;
            doNothing().when(userService).deleteById(USER_ID, v);

            mvc.perform(delete(API + "/{userId}", USER_ID).accept(JSON).header("If-Match", convertToEtag(v)))
               .andExpect(status().isNoContent())
               .andExpect(header().doesNotExist("ETag"))
               .andExpect(content().string(""));
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("428 Precondition Required: missing If-Match")
        void returns428(Locale locale) throws Exception {
            mvc.perform(delete(API + "/{userId}", USER_ID).accept(JSON).locale(locale))
               .andExpect(status().isPreconditionRequired());
        }

        @Test
        @DisplayName("400 Bad Request: invalid If-Match")
        void returns400_when_wrong_etag() throws Exception {
            mvc.perform(delete(API + "/{userId}", USER_ID).accept(JSON).header("If-Match", "bad"))
               .andExpect(status().isBadRequest());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("412 Precondition Failed: version mismatch")
        void returns412(Locale locale) throws Exception {
            long current = 5L, expected = 10L;
            doThrow(new MatchFailedException(RequiredHeaderError.MATCH_FAILED, current, expected))
                    .when(userService).deleteById(USER_ID, current);

            mvc.perform(delete(API + "/{userId}", USER_ID).accept(JSON).locale(locale).header("If-Match", convertToEtag(current)))
               .andExpect(status().isPreconditionFailed());
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.iam.UserControllerTest#locales")
        @DisplayName("404 Not Found")
        void returns404(Locale locale) throws Exception {
            long v = 9L;
            doThrow(new NotFoundException(UserError.NOT_FOUND_BY_ID, USER_ID)).when(userService).deleteById(USER_ID, v);

            mvc.perform(delete(API + "/{userId}", USER_ID).accept(JSON).locale(locale).header("If-Match", convertToEtag(v)))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.message").value(createMessage(
                       locale,
                       "User with ID=%s not found",
                       "Пользователь с ID=%s не найден",
                       USER_ID)));
        }
    }

    // -------- helpers --------

    private UserDTO createUserDto(Integer id, String username, String email, Long version) {
        return new UserDTO(
                id,
                username,
                email,
                new UserRole(),
                RegistrationStatus.ACTIVE,
                List.of(new RoleDTO(1L, "USER")),
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                version
        );
    }

    private String asJson(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }

    private String convertToEtag(long v) {
        return EtagUtils.toEtag(v);
    }

    static Stream<Locale> locales() {
        return Stream.of(Locale.ENGLISH, Locale.of("ru", "RU"));
    }

    private static String createMessage(Locale locale, String en, String ru, Object... args) {
        String pattern = locale.getLanguage().equals("ru") ? ru : en;
        return args.length == 0 ? pattern : String.format(pattern, args);
    }
}
