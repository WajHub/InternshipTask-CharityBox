package pl.wajhub.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.mapper.FundraisingEventMapper;
import pl.wajhub.server.service.FundraisingEventService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(FundraisingEventController.class)
class FundraisingEventControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FundraisingEventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createEvent() throws Exception {
        FundraisingEventDtoRequest eventDtoRequest = new FundraisingEventDtoRequest("Charity Event");

        String requestBody = objectMapper.writeValueAsString(eventDtoRequest);

        mockMvc.perform(post("/api/v1/events")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void createEventEmptyName() throws Exception {
        FundraisingEventDtoRequest eventDtoRequest = new FundraisingEventDtoRequest("");

        String requestBody = objectMapper.writeValueAsString(eventDtoRequest);

        mockMvc.perform(post("/api/v1/events")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}