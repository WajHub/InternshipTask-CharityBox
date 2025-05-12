package pl.wajhub.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.exception.EventDuplicateNameException;
import pl.wajhub.server.service.CollectionBoxService;
import pl.wajhub.server.service.FundraisingEventService;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(FundraisingEventController.class)
@ExtendWith(MockitoExtension.class)
class FundraisingEventControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private FundraisingEventService eventService;
    @MockitoBean
    private CollectionBoxService collectionBoxService;

    private FundraisingEventDtoRequest eventDtoRequest;

    @BeforeEach
    public void init(){
        eventDtoRequest = new FundraisingEventDtoRequest("Charity Event", "PLN");
    }

    @Test
    public void getCollections_SuccessfullyReturn_EventsList() throws Exception {
        List<FundraisingEventDtoResponse> events =
                List.of(
                        FundraisingEventDtoResponse.builder().uuid(UUID.randomUUID()).name("Charity one").currencyCode("PLN").balance(10.1).build(),
                        FundraisingEventDtoResponse.builder().uuid(UUID.randomUUID()).name("Charity two").currencyCode("PLN").balance(104.1).build(),
                        FundraisingEventDtoResponse.builder().uuid(UUID.randomUUID()).name("Test1").currencyCode("USD").balance(110.1).build(),
                        FundraisingEventDtoResponse.builder().uuid(UUID.randomUUID()).name("Test2").currencyCode("EUR").balance(120.1).build()
                );

        when(eventService.getAll()).thenReturn(events);
        ResultActions response =
                mockMvc.perform(get("/api/v1/events"));

        MvcResult result = response.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        List<FundraisingEventDtoResponse> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<FundraisingEventDtoResponse>>(){} );
        assertEquals(events.size(), actual.size());
        for (int i = 0; i < events.size(); i++) {
            assertEquals(events.get(i).uuid(), actual.get(i).uuid());
            assertEquals(events.get(i).name(), actual.get(i).name());
            assertEquals(events.get(i).balance(), actual.get(i).balance());
            assertEquals(events.get(i).currencyCode(), actual.get(i).currencyCode());
        }
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

    @Test
    public void createEvent_ThrowException_EmptyName() throws Exception {
        FundraisingEventDtoRequest eventDtoRequest = new FundraisingEventDtoRequest("","PLN");
        String requestBody = objectMapper.writeValueAsString(eventDtoRequest);

        mockMvc.perform(post("/api/v1/events")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void createEvent_ThrowException_DuplicateName() throws Exception {
        String requestBody = objectMapper.writeValueAsString(eventDtoRequest);
        when(eventService.create(eventDtoRequest)).thenThrow(EventDuplicateNameException.class);

        mockMvc.perform(post("/api/v1/events")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void transferMoney_SuccessfullyTransfer_StandardTransfer() throws Exception {
        UUID eventUuid = UUID.randomUUID();
        UUID collectionUuid = UUID.randomUUID();
        FundraisingEventDtoResponse dtoResponse =
                FundraisingEventDtoResponse.builder()
                        .uuid(eventUuid)
                        .name("Charity One")
                        .currencyCode("PLN")
                        .balance(100.0)
                        .build();
        when(eventService.transfer(eventUuid, collectionUuid)).thenReturn(dtoResponse);

        ResultActions response =
                mockMvc.perform(patch("/api/v1/events/"+eventUuid+"/collections/"+collectionUuid+"/transfer"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(dtoResponse.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", CoreMatchers.is(dtoResponse.balance())));
    }
}