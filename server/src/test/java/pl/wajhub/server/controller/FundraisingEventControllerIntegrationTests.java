package pl.wajhub.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.exception.EventDuplicateNameException;
import pl.wajhub.server.model.FundraisingEvent;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class FundraisingEventControllerIntegrationTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public FundraisingEventControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void createEvent_SuccessfullyCreated_NewEvent() throws Exception {
        String newName = "Charity One - New Event test";
        FundraisingEventDtoRequest eventDtoRequest = FundraisingEventDtoRequest.builder()
                .name(newName)
                .currencyCode("EUR")
                .build();
        String requestBody = objectMapper.writeValueAsString(eventDtoRequest);

        ResultActions response =
                mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(newName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", CoreMatchers.is(0.0)));
    }

    @Test
    public void createEvent_ThrowException_DuplicateName() throws Exception {
        FundraisingEventDtoRequest eventDtoRequest = FundraisingEventDtoRequest.builder()
                .name("Charity One")
                .currencyCode("EUR")
                .build();
        String requestBody = objectMapper.writeValueAsString(eventDtoRequest);

        ResultActions response =
                mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertInstanceOf(EventDuplicateNameException.class, result.getResolvedException()));
    }

    @Test
    public void createEvent_ThrowException_IncorrectCurrencyCode() throws Exception {
        FundraisingEventDtoRequest eventDtoRequest = FundraisingEventDtoRequest.builder()
                .name("Charity One")
                .currencyCode("ERR")
                .build();
        String requestBody = objectMapper.writeValueAsString(eventDtoRequest);

        ResultActions response =
                mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
}