package io.github.artsobol.kurkod.web.controller.chickenmovement;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.artsobol.kurkod.security.filter.JwtRequestFilter;
import io.github.artsobol.kurkod.security.jwt.JwtTokenProvider;
import io.github.artsobol.kurkod.web.advice.CommonControllerAdvice;
import io.github.artsobol.kurkod.web.domain.chickenmovement.error.ChickenMovementError;
import io.github.artsobol.kurkod.common.exception.NotFoundException;
import io.github.artsobol.kurkod.web.domain.chickenmovement.model.dto.ChickenMovementDTO;
import io.github.artsobol.kurkod.web.domain.chickenmovement.model.request.ChickenMovementPostRequest;
import io.github.artsobol.kurkod.web.domain.chickenmovement.service.api.ChickenMovementService;
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
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ChickenMovementController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({CommonControllerAdvice.class})
class ChickenMovementControllerTest {

    private static final MediaType JSON = MediaType.APPLICATION_JSON;

    private static final String API_ROOT = "/api/v1";
    private static final String API_MOVEMENT_BY_ID = API_ROOT + "/chicken-movements/{id}";
    private static final String API_CHICKEN_MOVEMENTS = API_ROOT + "/chickens/{chickenId}/movements";
    private static final String API_CHICKEN_MOVEMENTS_CURRENT = API_ROOT + "/chickens/{chickenId}/movements/current";

    private static final int MOVEMENT_ID = 15;
    private static final int CHICKEN_ID = 7;
    private static final int FROM_CAGE_ID = 100;
    private static final int TO_CAGE_ID = 200;

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean ChickenMovementService chickenMovementService;
    @MockitoBean JwtTokenProvider jwtTokenProvider;
    @MockitoBean JwtRequestFilter jwtRequestFilter;

    @Nested
    class GetById {
        @Test
        @DisplayName("200 OK: возвращает один movement")
        void returns200_with_payload() throws Exception {
            ChickenMovementDTO dto = createDto(MOVEMENT_ID, CHICKEN_ID, FROM_CAGE_ID, TO_CAGE_ID, OffsetDateTime.parse("2024-05-10T12:00:00Z"));
            when(chickenMovementService.get(MOVEMENT_ID)).thenReturn(dto);

            mvc.perform(get(API_MOVEMENT_BY_ID, MOVEMENT_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(MOVEMENT_ID))
               .andExpect(jsonPath("$.payload.chickenId").value(CHICKEN_ID))
               .andExpect(jsonPath("$.payload.fromCageId").value(FROM_CAGE_ID))
               .andExpect(jsonPath("$.payload.toCageId").value(TO_CAGE_ID));

            verify(chickenMovementService).get(MOVEMENT_ID);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.chickenmovement.ChickenMovementControllerTest#locales")
        @DisplayName("404 Not Found: не найден по id")
        void returns404(Locale locale) throws Exception {
            when(chickenMovementService.get(MOVEMENT_ID))
                    .thenThrow(new NotFoundException(ChickenMovementError.NOT_FOUND_BY_ID, MOVEMENT_ID));

            mvc.perform(get(API_MOVEMENT_BY_ID, MOVEMENT_ID).accept(JSON).locale(locale))
               .andExpect(status().isNotFound());

            verify(chickenMovementService).get(MOVEMENT_ID);
        }
    }

    @Nested
    class GetAllByChicken {
        @Test
        @DisplayName("200 OK: список движений по курице")
        void returns200_list() throws Exception {
            List<ChickenMovementDTO> list = List.of(
                    createDto(1, CHICKEN_ID, 10, 20, OffsetDateTime.parse("2024-05-10T12:00:00Z")),
                    createDto(2, CHICKEN_ID, 20, 30, OffsetDateTime.parse("2024-06-01T09:00:00Z"))
                                                   );
            when(chickenMovementService.getAllByChickenId(CHICKEN_ID)).thenReturn(list);

            mvc.perform(get(API_CHICKEN_MOVEMENTS, CHICKEN_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(2)))
               .andExpect(jsonPath("$.payload[0].id").value(1))
               .andExpect(jsonPath("$.payload[1].id").value(2));

            verify(chickenMovementService).getAllByChickenId(CHICKEN_ID);
        }

        @Test
        @DisplayName("200 OK: пустой список")
        void returns200_empty_list() throws Exception {
            when(chickenMovementService.getAllByChickenId(CHICKEN_ID)).thenReturn(List.of());

            mvc.perform(get(API_CHICKEN_MOVEMENTS, CHICKEN_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload", hasSize(0)));

            verify(chickenMovementService).getAllByChickenId(CHICKEN_ID);
        }
    }

    @Nested
    class GetCurrent {
        @Test
        @DisplayName("200 OK: текущее движение")
        void returns200_current() throws Exception {
            ChickenMovementDTO dto = createDto(33, CHICKEN_ID, 50, 60, OffsetDateTime.parse("2024-07-01T08:30:00Z"));
            when(chickenMovementService.getCurrentCage(CHICKEN_ID)).thenReturn(dto);

            mvc.perform(get(API_CHICKEN_MOVEMENTS_CURRENT, CHICKEN_ID).accept(JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(33))
               .andExpect(jsonPath("$.payload.toCageId").value(60));

            verify(chickenMovementService).getCurrentCage(CHICKEN_ID);
        }

        @ParameterizedTest(name = "[{index}] locale={0}")
        @MethodSource("io.github.artsobol.kurkod.web.controller.chickenmovement.ChickenMovementControllerTest#locales")
        @DisplayName("404 Not Found: нет текущего перемещения")
        void returns404(Locale locale) throws Exception {
            when(chickenMovementService.getCurrentCage(CHICKEN_ID))
                    .thenThrow(new NotFoundException(ChickenMovementError.NOT_FOUND_BY_ID, CHICKEN_ID));

            mvc.perform(get(API_CHICKEN_MOVEMENTS_CURRENT, CHICKEN_ID).accept(JSON).locale(locale))
               .andExpect(status().isNotFound());

            verify(chickenMovementService).getCurrentCage(CHICKEN_ID);
        }
    }

    @Nested
    class Create {
        @Test
        @DisplayName("201 Created: Location + payload")
        void returns201_with_location_and_payload() throws Exception {
            ChickenMovementPostRequest request = createPostRequest(null, TO_CAGE_ID, "2024-08-10T10:15:00Z");
            ChickenMovementDTO dto = createDto(101, CHICKEN_ID, null, TO_CAGE_ID, OffsetDateTime.parse("2024-08-10T10:15:00Z"));

            when(chickenMovementService.create(eq(CHICKEN_ID), any(ChickenMovementPostRequest.class))).thenReturn(dto);

            mvc.perform(post(API_CHICKEN_MOVEMENTS, CHICKEN_ID).accept(JSON)
                                                               .contentType(JSON)
                                                               .content(toJson(request)))
               .andExpect(status().isCreated())
               .andExpect(content().contentTypeCompatibleWith(JSON))
               .andExpect(header().string("Location", endsWith(API_CHICKEN_MOVEMENTS.replace("{chickenId}", "" + CHICKEN_ID))))
               .andExpect(jsonPath("$.success").value(true))
               .andExpect(jsonPath("$.payload.id").value(101))
               .andExpect(jsonPath("$.payload.toCageId").value(TO_CAGE_ID));

            verify(chickenMovementService).create(eq(CHICKEN_ID), any(ChickenMovementPostRequest.class));
        }

        @Test
        @DisplayName("400 Bad Request: пустое тело")
        void returns400_empty_body() throws Exception {
            mvc.perform(post(API_CHICKEN_MOVEMENTS, CHICKEN_ID).accept(JSON).contentType(JSON))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(chickenMovementService);
        }

        @Test
        @DisplayName("400 Bad Request: невалидное тело (нет toCageId)")
        void returns400_invalid_body() throws Exception {
            ChickenMovementPostRequest bad = new ChickenMovementPostRequest();
            bad.setFromCageId(FROM_CAGE_ID);

            mvc.perform(post(API_CHICKEN_MOVEMENTS, CHICKEN_ID).accept(JSON)
                                                               .contentType(JSON)
                                                               .content(toJson(bad)))
               .andExpect(status().isBadRequest())
               .andExpect(content().contentTypeCompatibleWith(JSON));

            verifyNoInteractions(chickenMovementService);
        }

        @Test
        @DisplayName("400 Bad Request: битый JSON")
        void returns400_malformed_json() throws Exception {
            mvc.perform(post(API_CHICKEN_MOVEMENTS, CHICKEN_ID).accept(JSON)
                                                               .contentType(JSON)
                                                               .content("{"))
               .andExpect(status().isBadRequest());

            verifyNoInteractions(chickenMovementService);
        }

        @Test
        @DisplayName("415 Unsupported Media Type: неверный Content-Type")
        void returns415_wrong_content_type() throws Exception {
            ChickenMovementPostRequest request = createPostRequest(FROM_CAGE_ID, TO_CAGE_ID, "2024-08-10T10:15:00Z");

            mvc.perform(post(API_CHICKEN_MOVEMENTS, CHICKEN_ID).accept(JSON)
                                                               .contentType(MediaType.TEXT_PLAIN)
                                                               .content("toCageId=200"))
               .andExpect(status().isUnsupportedMediaType());

            verifyNoInteractions(chickenMovementService);
        }
    }

    private ChickenMovementDTO createDto(Integer id, Integer chickenId, Integer fromCageId, Integer toCageId, OffsetDateTime movedAt) {
        return new ChickenMovementDTO(id, chickenId, fromCageId, toCageId, movedAt);
    }

    private ChickenMovementPostRequest createPostRequest(Integer fromCageId, Integer toCageId, String movedAtIso) {
        ChickenMovementPostRequest r = new ChickenMovementPostRequest();
        r.setFromCageId(fromCageId);
        r.setToCageId(toCageId);
        r.setMovedAt(movedAtIso == null ? null : OffsetDateTime.parse(movedAtIso));
        return r;
    }

    private String toJson(Object o) throws Exception {
        return objectMapper.writeValueAsString(o);
    }

    static Stream<Locale> locales() {
        return Stream.of(Locale.ENGLISH, Locale.of("ru", "RU"));
    }
}
