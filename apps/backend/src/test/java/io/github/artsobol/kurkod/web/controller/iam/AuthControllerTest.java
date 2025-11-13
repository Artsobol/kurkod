package io.github.artsobol.kurkod.web.controller.iam;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.cookie.CookieFactory;
import io.github.artsobol.kurkod.web.domain.auth.service.api.AuthService;
import io.github.artsobol.kurkod.web.domain.iam.auth.model.request.LoginRequest;
import io.github.artsobol.kurkod.web.domain.iam.auth.model.request.RegistrationRequest;
import io.github.artsobol.kurkod.web.domain.iam.user.model.dto.UserProfileDTO;
import io.github.artsobol.kurkod.web.response.IamResponse;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(CommonControllerAdvice.class)
class AuthControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @MockitoBean AuthService authService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    @Nested
    class Login {
        @Test
        @DisplayName("200 OK: body + Set-Cookie")
        void returns200_with_cookie() throws Exception {
            String token = "jwt-token-123";
            UserProfileDTO payload = UserProfileDTO.builder().token(token).username("john").build();
            when(authService.login(any(LoginRequest.class)))
                    .thenReturn(IamResponse.createSuccessful(payload));

            Cookie authCookie = buildCookie("Authorization", token);
            try (MockedStatic<CookieFactory> mocked = Mockito.mockStatic(CookieFactory.class)) {
                mocked.when(() -> CookieFactory.createAuthCookie(token)).thenReturn(authCookie);

                LoginRequest req = new LoginRequest("john@example.com", "secret");
                mvc.perform(post("/auth/login").accept(JSON).contentType(JSON).content(om.writeValueAsString(req)))
                   .andExpect(status().isOk())
                   .andExpect(content().contentTypeCompatibleWith(JSON))
                   .andExpect(cookie().exists("Authorization"))
                   .andExpect(cookie().value("Authorization", token))
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.payload.token").value(token))
                   .andExpect(jsonPath("$.payload.username").value("john"));

                verify(authService).login(any(LoginRequest.class));
                mocked.verify(() -> CookieFactory.createAuthCookie(token));
            }
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_on_invalid_body() throws Exception {
            LoginRequest bad = new LoginRequest("not-an-email", ""); // триггер валидации

            mvc.perform(post("/auth/login").accept(JSON).contentType(JSON).content(om.writeValueAsString(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));
        }

        @Test
        @DisplayName("415 Unsupported Media Type")
        void returns415_on_wrong_content_type() throws Exception {
            mvc.perform(post("/auth/login").accept(JSON)
                                           .contentType(MediaType.TEXT_PLAIN)
                                           .content("email=john@example.com&password=secret"))
               .andExpect(status().isUnsupportedMediaType());
        }
    }

    @Nested
    class Refresh {
        @Test
        @DisplayName("200 OK: body + Set-Cookie")
        void returns200_with_cookie() throws Exception {
            String refresh = "refresh-abc";
            String token = "jwt-token-xyz";
            UserProfileDTO payload = UserProfileDTO.builder().token(token).username("john").build();
            when(authService.refreshAccessToken(refresh))
                    .thenReturn(IamResponse.createSuccessful(payload));

            Cookie authCookie = buildCookie("Authorization", token);
            try (MockedStatic<CookieFactory> mocked = Mockito.mockStatic(CookieFactory.class)) {
                mocked.when(() -> CookieFactory.createAuthCookie(token)).thenReturn(authCookie);

                mvc.perform(get("/auth/refresh/token").accept(JSON).param("token", refresh))
                   .andExpect(status().isOk())
                   .andExpect(content().contentTypeCompatibleWith(JSON))
                   .andExpect(cookie().exists("Authorization"))
                   .andExpect(cookie().value("Authorization", token))
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.payload.token").value(token));

                verify(authService).refreshAccessToken(refresh);
                mocked.verify(() -> CookieFactory.createAuthCookie(token));
            }
        }

        @Test
        @DisplayName("400 Bad Request: missing token param")
        void returns400_when_param_missing() throws Exception {
            mvc.perform(get("/auth/refresh/token").accept(JSON))
               .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class Register {
        @Test
        @DisplayName("200 OK: body + Set-Cookie")
        void returns200_with_cookie() throws Exception {
            String token = "jwt-new-user";
            UserProfileDTO payload = UserProfileDTO.builder().token(token).username("alice").build();
            when(authService.registerUser(any(RegistrationRequest.class)))
                    .thenReturn(IamResponse.createSuccessful(payload));

            Cookie authCookie = buildCookie("Authorization", token);
            try (MockedStatic<CookieFactory> mocked = Mockito.mockStatic(CookieFactory.class)) {
                mocked.when(() -> CookieFactory.createAuthCookie(token)).thenReturn(authCookie);

                RegistrationRequest req = new RegistrationRequest(
                        "alice",
                        "alice@example.com",
                        "secret",
                        "secret" // confirmPassword
                );
                mvc.perform(post("/auth/register").accept(JSON).contentType(JSON).content(om.writeValueAsString(req)))
                   .andExpect(status().isOk())
                   .andExpect(content().contentTypeCompatibleWith(JSON))
                   .andExpect(cookie().exists("Authorization"))
                   .andExpect(cookie().value("Authorization", token))
                   .andExpect(jsonPath("$.success").value(true))
                   .andExpect(jsonPath("$.payload.username").value("alice"));

                verify(authService).registerUser(any(RegistrationRequest.class));
                mocked.verify(() -> CookieFactory.createAuthCookie(token));
            }
        }

        @Test
        @DisplayName("400 Bad Request: invalid body")
        void returns400_on_invalid_body() throws Exception {
            RegistrationRequest bad = new RegistrationRequest("", "bad-email", "", "mismatch");
            mvc.perform(post("/auth/register").accept(JSON).contentType(JSON).content(om.writeValueAsString(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));
        }
    }

    private Cookie buildCookie(String name, String value) {
        Cookie c = new Cookie(name, value);
        c.setPath("/");
        c.setHttpOnly(true);
        c.setSecure(true);
        return c;
    }
}
