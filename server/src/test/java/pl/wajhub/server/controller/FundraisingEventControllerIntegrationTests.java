package pl.wajhub.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class FundraisingEventControllerIntegrationTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private FundraisingEventDtoRequest eventDtoRequest;


    @Autowired
    public FundraisingEventControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void init(){
        eventDtoRequest = new FundraisingEventDtoRequest("Charity Event", "PLN");
    }

    @Test
    public void transferMoney_SuccessfullyTransfer_StandardTransfer() throws Exception {
        UUID eventUuid = UUID.fromString("66eaa713-c6a8-47c6-98fa-da78bfab9376");
        UUID collectionUuid = UUID.fromString("be4c9355-bac8-4262-84f9-07cc1eb1a192");
        FundraisingEventDtoResponse dtoResponse =
                FundraisingEventDtoResponse.builder()
                        .uuid(eventUuid)
                        .name("Charity One")
                        .currencyCode("PLN")
                        .balance(100.0)
                        .build();

        ResultActions response =
                mockMvc.perform(patch("/api/v1/events/"+eventUuid+"/collections/"+collectionUuid+"/transfer"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dtoResponse.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", CoreMatchers.is(dtoResponse.balance())));
    }

}