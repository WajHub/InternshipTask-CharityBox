package pl.wajhub.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.exception.EventDuplicateNameException;
import pl.wajhub.server.service.CollectionBoxService;
import pl.wajhub.server.service.FundraisingEventService;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class FundraisingEventControllerIntergationTests {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private FundraisingEventDtoRequest eventDtoRequest;


    @Autowired
    public FundraisingEventControllerIntergationTests(MockMvc mockMvc,ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void init(){
        eventDtoRequest = new FundraisingEventDtoRequest("Charity Event", "PLN");
    }

    @Test
    public void createEvent_SuccessfullyCreated_NewEvent() throws Exception {
        String requestBody = objectMapper.writeValueAsString(eventDtoRequest);

        ResultActions response =
                mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody));

        response.andExpect(MockMvcResultMatchers.status().isCreated());
    }
}