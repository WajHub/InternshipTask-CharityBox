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
import pl.wajhub.server.model.FundraisingEvent;

import java.util.UUID;

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
    public void transferMoney_SuccessfullyTransfer_StandardTransfer() throws Exception {
        UUID eventUuid = UUID.fromString("66eaa713-c6a8-47c6-98fa-da78bfab9376");
        UUID collectionUuid = UUID.fromString("be4c9355-bac8-4262-84f9-07cc1eb1a192");
        FundraisingEventDtoResponse dtoResponse =
                FundraisingEventDtoResponse.builder()
                        .uuid(eventUuid)
                        .name("Charity One")
                        .currencyCode("EUR")
                        .balance(100.0)
                        .build();

        ResultActions response =
                mockMvc.perform(patch("/api/v1/events/"+eventUuid+"/collections/"+collectionUuid+"/transfer"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dtoResponse.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", CoreMatchers.is(dtoResponse.balance())));
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