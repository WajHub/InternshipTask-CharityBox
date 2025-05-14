package pl.wajhub.server.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.request.PutMoneyInCollectionBoxRequest;
import pl.wajhub.server.repository.CollectionBoxRepository;
import pl.wajhub.server.repository.FundraisingEventRepository;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.greaterThan;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationEndToEndTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private FundraisingEventRepository eventRepository;

    @Autowired
    private CollectionBoxRepository boxRepository;

    @BeforeEach
    public void setup() {
        boxRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @Order(1)
    void createEventAndCollectionBox_PutMoney_Transfer_Successfully() throws Exception {
        // Create event ------------------------------
        UUID eventUuid = UUID.randomUUID();
        FundraisingEventDtoRequest eventDtoRequest =
                FundraisingEventDtoRequest.builder()
                        .name("TEST_Event")
                        .currencyCode("PLN")
                        .build();

        String createEvent_RequestBody = objectMapper.writeValueAsString(eventDtoRequest);

        mockMvc.perform(put("/api/v1/events/"+eventUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createEvent_RequestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(eventDtoRequest.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance", CoreMatchers.is(0.0)));
        //-----------------------------------------------

        // Create Collection Boxes ----------------------
        UUID collectionBox1Uuid = UUID.randomUUID();
        UUID collectionBox2Uuid = UUID.randomUUID();

        mockMvc.perform(put("/api/v1/collections/"+collectionBox1Uuid))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEmpty", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAssigned", CoreMatchers.is(false)));

        mockMvc.perform(put("/api/v1/collections/"+collectionBox2Uuid))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEmpty", CoreMatchers.is(true)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAssigned", CoreMatchers.is(false)));
        //-------------------------------------------------

        // Register collection boxes ---------------------
        mockMvc.perform(patch("/api/v1/events/"+eventUuid+"/collections/"+collectionBox1Uuid+"/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAssigned", CoreMatchers.is(true)));

        mockMvc.perform(patch("/api/v1/events/"+eventUuid+"/collections/"+collectionBox2Uuid+"/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAssigned", CoreMatchers.is(true)));
        // ------------------------------------------------

        // Put Money in Collection boxes ------------------
        PutMoneyInCollectionBoxRequest putMoneyInCollectionBox1_Request1 =
                PutMoneyInCollectionBoxRequest.builder()
                        .amount(10.0)
                        .currencyCode("PLN")
                        .build();

        PutMoneyInCollectionBoxRequest putMoneyInCollectionBox1_Request2 =
                PutMoneyInCollectionBoxRequest.builder()
                        .amount(20.0)
                        .currencyCode("EUR")
                        .build();

        PutMoneyInCollectionBoxRequest putMoneyInCollectionBox2_Request1 =
                PutMoneyInCollectionBoxRequest.builder()
                        .amount(15.0)
                        .currencyCode("PLN")
                        .build();

        mockMvc.perform(patch("/api/v1/collections/" + collectionBox1Uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(putMoneyInCollectionBox1_Request1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEmpty", CoreMatchers.is(false)));
        mockMvc.perform(patch("/api/v1/collections/" + collectionBox1Uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(putMoneyInCollectionBox1_Request2)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEmpty", CoreMatchers.is(false)));

        mockMvc.perform(patch("/api/v1/collections/" + collectionBox2Uuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(putMoneyInCollectionBox2_Request1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEmpty", CoreMatchers.is(false)));
        // ----------------------------------------------

        // Transfer -------------------------------------
        mockMvc.perform(patch("/api/v1/collections/" +collectionBox1Uuid + "/transfer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEmpty", CoreMatchers.is(true)));

        mockMvc.perform(patch("/api/v1/collections/" +collectionBox2Uuid + "/transfer"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEmpty", CoreMatchers.is(true)));
        // -----------------------------------------------

        // Results in Event ------------------------------
        mockMvc.perform(get("/api/v1/events"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", CoreMatchers.is(eventDtoRequest.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance", greaterThan(0.0)));
        // -----------------------------------------------
    }

}
