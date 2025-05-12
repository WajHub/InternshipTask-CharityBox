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
import pl.wajhub.server.model.CollectionBox;

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

}