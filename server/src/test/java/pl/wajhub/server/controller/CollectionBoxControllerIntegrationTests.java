package pl.wajhub.server.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class CollectionBoxControllerIntegrationTests {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public CollectionBoxControllerIntegrationTests(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void registerCollection_SuccessfullyRegistered_NewCollection() throws Exception {
        ResultActions response =
                mockMvc.perform(patch("/api/v1/events/56eb1354-a780-446f-beb9-705602f25104/collections/5a65a78b-e765-4b26-92ea-4903124ae19c/register"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAssigned", CoreMatchers.is(true)));

    }

    @Test
    public void transferMoney_SuccessfullyTransfer_StandardTransfer() throws Exception {
        UUID collectionUuid = UUID.fromString("be4c9355-bac8-4262-84f9-07cc1eb1a192");
        CollectionBoxDtoResponse collectionBoxDtoResponse =
                CollectionBoxDtoResponse.builder()
                        .isAssigned(true)
                        .isEmpty(true)
                        .build();

        ResultActions response =
                mockMvc.perform(patch("/api/v1/collections/"+collectionUuid+"/transfer"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEmpty", CoreMatchers.is(collectionBoxDtoResponse.isEmpty())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAssigned", CoreMatchers.is(collectionBoxDtoResponse.isAssigned())));
    }

}