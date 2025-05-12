package pl.wajhub.server.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.wajhub.server.dto.request.PutMoneyInCollectionBoxRequest;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.service.CollectionBoxService;


import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CollectionBoxController.class)
@ExtendWith(MockitoExtension.class)
public class CollectionBoxControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CollectionBoxService collectionBoxService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getCollection_SuccessfullyReturned_ListOfCollections() throws Exception{
        List<CollectionBoxDtoResponse> collections =
            List.of(
                CollectionBoxDtoResponse.builder().uuid(UUID.randomUUID()).isEmpty(true).isAssigned(true).build(),
                CollectionBoxDtoResponse.builder().uuid(UUID.randomUUID()).isEmpty(true).isAssigned(true).build(),
                CollectionBoxDtoResponse.builder().uuid(UUID.randomUUID()).isEmpty(true).isAssigned(true).build(),
                CollectionBoxDtoResponse.builder().uuid(UUID.randomUUID()).isEmpty(true).isAssigned(true).build()
        );

        when(collectionBoxService.getAll()).thenReturn(collections);

        MvcResult result = mockMvc.perform(get("/api/v1/collections"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();


        List<CollectionBoxDtoResponse> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<CollectionBoxDtoResponse>>(){} );
        assertEquals(collections.size(), actual.size());
        for (int i = 0; i < collections.size(); i++) {
            assertEquals(collections.get(i).uuid(), actual.get(i).uuid());
            assertEquals(collections.get(i).isEmpty(), actual.get(i).isEmpty());
            assertEquals(collections.get(i).isAssigned(), actual.get(i).isAssigned());
        }
    }

    @Test
    public void createCollection_SuccessfullyCreated_NewCollection() throws Exception {
        mockMvc.perform(post("/api/v1/collections"))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void putMoney_SuccessfullyPut_NewCurrency() throws Exception {
        CollectionBoxDtoResponse dtoResponse =  CollectionBoxDtoResponse.builder()
                .uuid(UUID.randomUUID())
                .isEmpty(false)
                .isAssigned(true)
                .build();
        PutMoneyInCollectionBoxRequest request = PutMoneyInCollectionBoxRequest.builder()
                .currencyCode("PLN")
                .amount(10.0)
                .build();
        String requestBody = objectMapper.writeValueAsString(request);

        when(collectionBoxService.putMoney(dtoResponse.uuid(), request)).thenReturn(dtoResponse);

        mockMvc.perform(patch("/api/v1/collections/"+dtoResponse.uuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEmpty", CoreMatchers.is(dtoResponse.isEmpty())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAssigned", CoreMatchers.is(dtoResponse.isAssigned())));
    }

    @Test
    public void putMoney_ThrowException_IncorrectCurrencyCode() throws Exception {
        CollectionBoxDtoResponse dtoResponse =  CollectionBoxDtoResponse.builder()
                .uuid(UUID.randomUUID())
                .isEmpty(false)
                .isAssigned(true)
                .build();
        PutMoneyInCollectionBoxRequest request = PutMoneyInCollectionBoxRequest.builder()
                .currencyCode("dfgsdfgsdfg")
                .amount(10.0)
                .build();
        String requestBody = objectMapper.writeValueAsString(request);

        when(collectionBoxService.putMoney(dtoResponse.uuid(), request)).thenReturn(dtoResponse);

        mockMvc.perform(patch("/api/v1/collections/"+dtoResponse.uuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException()));
    }

    @Test
    public void transferMoney_SuccessfullyTransfer_StandardTransfer() throws Exception {
        UUID collectionUuid = UUID.randomUUID();
        CollectionBoxDtoResponse collectionBoxDtoResponse =
                CollectionBoxDtoResponse.builder()
                        .isEmpty(true)
                        .isAssigned(true)
                        .build();

        when(collectionBoxService.transfer(collectionUuid)).thenReturn(collectionBoxDtoResponse);

        ResultActions response =
                mockMvc.perform(patch("/api/v1/collections/"+collectionUuid+"/transfer"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAssigned", CoreMatchers.is(collectionBoxDtoResponse.isAssigned())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isEmpty", CoreMatchers.is(collectionBoxDtoResponse.isEmpty())));
    }
}