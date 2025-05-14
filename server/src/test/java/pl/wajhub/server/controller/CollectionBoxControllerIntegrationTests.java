package pl.wajhub.server.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.wajhub.server.dto.request.PutMoneyInCollectionBoxRequest;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.exception.CollectionBoxIsAlreadyRegisteredException;
import pl.wajhub.server.exception.CollectionBoxIsNotRegistered;
import pl.wajhub.server.exception.IncorrectMoneyValueException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

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
    public void transferMoney_ThrowException_NotRegisteredCollection() throws Exception{
        ResultActions response =
                mockMvc.perform(patch("/api/v1/collections/e329a2ca-d512-422d-a21c-9bbcbe034ef9/transfer"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertInstanceOf(CollectionBoxIsNotRegistered.class, (result.getResolvedException())));
    }

    @Test
    public void registerCollection_SuccessfullyRegistered_NewCollection() throws Exception {
        ResultActions response =
                mockMvc.perform(patch("/api/v1/events/56eb1354-a780-446f-beb9-705602f25104/collections/5a65a78b-e765-4b26-92ea-4903124ae19c/register"));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isAssigned", CoreMatchers.is(true)));
    }

    @Test
    public void registerCollection_ThrowException_AlreadyRegisteredCollection() throws Exception{
        ResultActions response =
                mockMvc.perform(patch("/api/v1/events/66eaa713-c6a8-47c6-98fa-da78bfab9376/collections/51ee5e36-8776-4c24-8815-1b7fc0dc1267/register"));

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertInstanceOf(CollectionBoxIsAlreadyRegisteredException.class, (result.getResolvedException())));
    }

    @Test
    public void putMoney_ThrowException__NegativeValue() throws Exception{
        PutMoneyInCollectionBoxRequest request = PutMoneyInCollectionBoxRequest.builder()
                .amount(-1.0)
                .currencyCode("PLN")
                .build();
        String requestBody = objectMapper.writeValueAsString(request);
        ResultActions response =
                mockMvc.perform(
                        patch("/api/v1/collections/5a65a78b-e765-4b26-92ea-4903124ae19c")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertInstanceOf(IncorrectMoneyValueException.class, (result.getResolvedException())));
    }

    @Test
    public void putMoney_ThrowException_IncorrectCurrencyCode() throws Exception{
        PutMoneyInCollectionBoxRequest request = PutMoneyInCollectionBoxRequest.builder()
                .amount(11.0)
                .currencyCode("ERR")
                .build();
        String requestBody = objectMapper.writeValueAsString(request);
        ResultActions response =
                mockMvc.perform(
                        patch("/api/v1/collections/5a65a78b-e765-4b26-92ea-4903124ae19c")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                );

        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(result -> assertInstanceOf(MethodArgumentNotValidException.class, (result.getResolvedException())));
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