package pl.wajhub.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wajhub.server.dto.request.PutMoneyInCollectionBoxRequest;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.exception.*;
import pl.wajhub.server.mapper.CollectionBoxMapper;
import pl.wajhub.server.model.CollectionBox;
import pl.wajhub.server.model.FundraisingEvent;
import pl.wajhub.server.repository.CollectionBoxRepository;
import pl.wajhub.server.repository.FundraisingEventRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CollectionBoxServiceUnitTests {

    @Mock
    private FundraisingEventRepository eventRepository;
    @Mock
    private CollectionBoxRepository collectionBoxRepository;
    @Mock
    private  CollectionBoxMapper collectionBoxMapper;
    @Mock
    private ExchangeService exchangeService;
    @InjectMocks
    private CollectionBoxService collectionBoxService;

    private FundraisingEvent event;
    private String eventCurrencyCode;
    private CollectionBox collectionBox;
    private CollectionBoxDtoResponse collectionBoxDtoResponse;

    @BeforeEach
    public void init(){
        eventCurrencyCode = "PLN";
        event = FundraisingEvent.builder()
                    .uuid(UUID.randomUUID())
                    .name("Charity One")
                    .currencyCode("PLN")
                .build();
        collectionBox = CollectionBox.builder()
                    .uuid(UUID.randomUUID())
                    .balance(new HashMap<>())
                .build();
        collectionBoxDtoResponse = CollectionBoxDtoResponse.builder()
                    .uuid(collectionBox.getUuid())
                .build();
    }

    @Test
    public void create_SuccessfullyCreated_NewCollectionBox() {
        Mockito.when(collectionBoxRepository.save(Mockito.any(CollectionBox.class))).thenReturn(collectionBox);
        Mockito.when(collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBox)).thenReturn(collectionBoxDtoResponse);

        CollectionBoxDtoResponse result = collectionBoxService.create();

        assertNotNull(result);
        assertEquals(collectionBox.getUuid(), result.uuid());
    }

    @Test
    public void create_SuccessfullyCreated_NewCollectionBoxWithUuid(){
        Mockito.when(collectionBoxRepository.save(Mockito.any(CollectionBox.class))).thenReturn(collectionBox);
        Mockito.when(collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBox)).thenReturn(collectionBoxDtoResponse);
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid())).thenReturn(Optional.empty());

        CollectionBoxDtoResponse result = collectionBoxService.create(collectionBox.getUuid());

        assertEquals(collectionBox.getUuid(), result.uuid());
        assertNotNull(result);
    }

    @Test
    public void create_ReturnExistingObject_NewCollectionBoxWithExistingUuid(){
        Mockito.when(collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBox)).thenReturn(collectionBoxDtoResponse);
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid())).thenReturn(Optional.of(collectionBox));

        CollectionBoxDtoResponse result = collectionBoxService.create(collectionBox.getUuid());

        assertEquals(collectionBox.getUuid(), result.uuid());
        assertNotNull(result);
    }

    @Test
    public void register_ThrowEventNotFoundException_RegisterToNotExistingEvent(){
        UUID randomUuid = UUID.randomUUID();
        Mockito.when(eventRepository.findById(randomUuid)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class,
                ()-> collectionBoxService.register(randomUuid, collectionBox.getUuid()));
    }

    @Test
    public void register_ThrowCollectionBoxNotFoundException_RegisterNotExistingCollectionBox(){
        UUID randomUuid = UUID.randomUUID();
        Mockito.when(eventRepository.findById(event.getUuid())).thenReturn(Optional.of(event));
        Mockito.when(collectionBoxRepository.findById(randomUuid)).thenReturn(Optional.empty());

        assertThrows(CollectionBoxNotFoundException.class,
                ()-> collectionBoxService.register(event.getUuid(), randomUuid));
    }

    @Test
    public void register_SuccessfullyRegisteredCollectionBoxToEvent_RegisterExistingBoxToExistingEvent(){
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid())).thenReturn(Optional.of(collectionBox));
        Mockito.when(eventRepository.findById(event.getUuid())).thenReturn(Optional.of(event));

        collectionBoxService.register(event.getUuid(),collectionBox.getUuid());
    }

    @Test
    public void register_UnsuccessfullyRegisterNotEmptyCollectionBox_NotEmptyCollectionBox(){
        collectionBox.setBalance(new HashMap<>(){{put("PLN", 10.0);}});
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid())).thenReturn(Optional.of(collectionBox));
        Mockito.when(eventRepository.findById(event.getUuid())).thenReturn(Optional.of(event));

        assertThrows(
                CollectionBoxIsNotEmptyException.class,
                () -> collectionBoxService.register(event.getUuid(), collectionBox.getUuid())
        );
    }

    @Test
    public void register_ThrowCollectionBoxIsAlreadyRegisteredException_RegisteredBox(){
        collectionBox.setEvent(FundraisingEvent.builder()
                .name("TEST")
                        .balance(0.0)
                        .currencyCode("PLN")
                .build());
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid())).thenReturn(Optional.of(collectionBox));
        Mockito.when(eventRepository.findById(event.getUuid())).thenReturn(Optional.of(event));

        assertThrows(
                CollectionBoxIsAlreadyRegisteredException.class,
                () -> collectionBoxService.register(event.getUuid(), collectionBox.getUuid())
        );
    }

    @Test
    public void unregister_ThrowCollectionBoxNotFoundException_UnregisterNotExistingCollectionBox(){
        UUID randomUuid = UUID.randomUUID();
        Mockito.when(collectionBoxRepository.findById(randomUuid)).thenReturn(Optional.empty());

        assertThrows(CollectionBoxNotFoundException.class,
                ()-> collectionBoxService.unregister(randomUuid));
    }

    @Test
    public void unregister_SuccessfullyUnregisterCollectionBox_UnregisterExistingBox(){
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid())).thenReturn(Optional.of(collectionBox));
        collectionBoxService.unregister(collectionBox.getUuid());

        Mockito.verify(collectionBoxRepository).delete(collectionBox);
    }

    @Test
    public void putMoney_SuccessfullyPutMoney_NewCurrencyInBox(){
        collectionBox.setEvent(event);
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid())).thenReturn(Optional.of(collectionBox));
        PutMoneyInCollectionBoxRequest request =
                PutMoneyInCollectionBoxRequest.builder()
                        .currencyCode("PLN")
                        .amount(10.0)
                    .build();

        collectionBoxService.putMoney(collectionBox.getUuid(), request);

        assertEquals(
                request.amount(),
                collectionBox.getBalance().get(request.currencyCode())
        );
    }

    @Test
    public void putMoney_SuccessfullyPutMoney_ExistingCurrencyInBox() {
        Double amountInBoxBeforeTransfer = 20.0;
        String currencyCodeInBox = "PLN";
        collectionBox.setEvent(event);
        collectionBox.setBalance(new HashMap<>() {{
            put(currencyCodeInBox, amountInBoxBeforeTransfer);
        }});
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid())).thenReturn(Optional.of(collectionBox));
        PutMoneyInCollectionBoxRequest request =
                PutMoneyInCollectionBoxRequest.builder()
                        .currencyCode(currencyCodeInBox)
                        .amount(10.0)
                        .build();

        collectionBoxService.putMoney(collectionBox.getUuid(), request);

        assertEquals(
                request.amount() + amountInBoxBeforeTransfer,
                collectionBox.getBalance().get(request.currencyCode())
        );
    }

    @Test
    public void putMoney_SuccessfullyPutMoney_MoreCurrenciesInBox(){
        Double amountInBoxBeforeTransfer = 20.0;
        String currencyCodeInBox = "PLN";
        collectionBox.setEvent(event);
        collectionBox.setBalance(new HashMap<>(){{
            put("EUR", 200.0);
            put(currencyCodeInBox, amountInBoxBeforeTransfer);
        }});
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid())).thenReturn(Optional.of(collectionBox));
        PutMoneyInCollectionBoxRequest request =
                PutMoneyInCollectionBoxRequest.builder()
                        .currencyCode(currencyCodeInBox)
                        .amount(10.0)
                        .build();

        collectionBoxService.putMoney(collectionBox.getUuid(), request);

        assertEquals(
                request.amount() + amountInBoxBeforeTransfer,
                collectionBox.getBalance().get(request.currencyCode())
        );
    }

    @Test
    public void putMoney_UnsuccessfullyPutMoney_NegativeAmountOfMoney(){
        collectionBox.setEvent(event);
        PutMoneyInCollectionBoxRequest request =
                PutMoneyInCollectionBoxRequest.builder()
                        .currencyCode("PLN")
                        .amount(-10.0)
                        .build();

        assertThrows(
                IncorrectMoneyValueException.class,
                ()->collectionBoxService.putMoney(collectionBox.getUuid(), request)
        );
    }

    @Test
    public void putMoney_UnsuccessfullyPutMoney_ZeroAmount(){
        collectionBox.setEvent(event);
        PutMoneyInCollectionBoxRequest request =
                PutMoneyInCollectionBoxRequest.builder()
                        .currencyCode("PLN")
                        .amount(0.0)
                        .build();

        assertThrows(
                IncorrectMoneyValueException.class,
                ()->collectionBoxService.putMoney(collectionBox.getUuid(), request)
        );
    }

    @Test
    public void putMoney_UnsuccessfullyPutMoney_NotAssignedEvent(){
        PutMoneyInCollectionBoxRequest request =
                PutMoneyInCollectionBoxRequest.builder()
                        .currencyCode("PLN")
                        .amount(10.0)
                        .build();
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid())).thenReturn(Optional.of(collectionBox));

        assertThrows(
                CollectionBoxIsNotAssigned.class,
                () -> collectionBoxService.putMoney(collectionBox.getUuid(), request)
        );
    }

    @Test
    public void transfer_SuccessfullyTransferred_TheSameCurrency(){
        Double amount = 100.0;
        Map<String, Double> balanceInCollectionBox = new HashMap<>(){
            {put(eventCurrencyCode,amount);}
        };
        CollectionBox collectionBox = CollectionBox.builder()
                .uuid(UUID.randomUUID())
                .event(event)
                .balance(balanceInCollectionBox)
                .build();
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid()))
                .thenReturn(Optional.of(collectionBox));

        collectionBoxService.transfer(collectionBox.getUuid());

        assertEquals(amount, event.getBalance());
    }

    @Test
    public void transfer_SuccessfullyTransferred_DifferentCurrency() throws IOException {
        double startAmount = 1.1;
        double amount = 250.0;
        double standardRate = 4.2;
        String destinationCurrencyCode = eventCurrencyCode;
        String sourceCurrencyCode = "EUR";

        event.setBalance(startAmount);
        CollectionBox collectionBox = CollectionBox.builder()
                .uuid(UUID.randomUUID())
                .event(event)
                .balance(new HashMap<>() {{
                    put(sourceCurrencyCode,amount);
                }})
                .build();

        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid()))
                .thenReturn(Optional.of(collectionBox));
        Mockito.when(exchangeService.getRate(sourceCurrencyCode, destinationCurrencyCode)).thenReturn(standardRate);

        collectionBoxService.transfer(collectionBox.getUuid());

        assertEquals(startAmount+amount*standardRate, event.getBalance());
    }

    @Test
    public void transfer_SuccessfullyTransferred_DifferentCurrencies() throws IOException {
        double amount = 250.0;
        double startAmount = 10.0;
        double standardRate = 4.2;
        String destinationCurrencyCode = eventCurrencyCode;
        String sourceCurrencyCode = "EUR";

        event.setBalance(startAmount);
        CollectionBox collectionBox = CollectionBox.builder()
                .uuid(UUID.randomUUID())
                .event(event)
                .balance(new HashMap<>() {{
                    put(sourceCurrencyCode,amount);
                    put(destinationCurrencyCode, amount);
                }})
                .build();

        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid()))
                .thenReturn(Optional.of(collectionBox));
        Mockito.when(exchangeService.getRate(sourceCurrencyCode, destinationCurrencyCode)).thenReturn(standardRate);

        collectionBoxService.transfer(collectionBox.getUuid());

        assertEquals(startAmount+amount+amount*standardRate, event.getBalance());
    }

    @Test
    public void transfer_SuccessfullyEmptiedCollectionsBox_DifferentCurrency() throws IOException {
        double amount = 250.0;
        String destinationCurrencyCode = eventCurrencyCode;
        String sourceCurrencyCode = "EUR";

        CollectionBox collectionBox = CollectionBox.builder()
                .uuid(UUID.randomUUID())
                .balance(new HashMap<>() {{
                    put(sourceCurrencyCode,amount);
                    put(destinationCurrencyCode, amount);
                }})
                .event(event)
                .build();

        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid()))
                .thenReturn(Optional.of(collectionBox));
        Mockito.when(exchangeService.getRate(sourceCurrencyCode, destinationCurrencyCode)).thenReturn(4.2);

        collectionBoxService.transfer(collectionBox.getUuid());
        double sumBalanceCollectionBox =
                collectionBox.getBalance().values()
                        .stream()
                        .mapToDouble(d-> d).sum();

        assertEquals(0.0, sumBalanceCollectionBox);
    }

    @Test
    public void transfer_Unsuccessfully_NotAssignedCollectionBox() {
        CollectionBox collectionBox = CollectionBox.builder()
                .uuid(UUID.randomUUID())
                .balance(new HashMap<>())
                .build();

        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid()))
                .thenReturn(Optional.of(collectionBox));

        assertThrows(
                CollectionBoxIsNotAssigned.class,
                () ->   collectionBoxService.transfer(collectionBox.getUuid())
        );
    }
}