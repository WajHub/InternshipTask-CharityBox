package pl.wajhub.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.exception.EventDuplicateNameException;
import pl.wajhub.server.mapper.FundraisingEventMapper;
import pl.wajhub.server.model.CollectionBox;
import pl.wajhub.server.model.FundraisingEvent;
import pl.wajhub.server.repository.CollectionBoxRepository;
import pl.wajhub.server.repository.FundraisingEventRepository;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FundraisingEventServiceTests {

    @Mock
    private FundraisingEventRepository eventRepository;

    @Mock
    private CollectionBoxRepository collectionBoxRepository;

    @Mock
    private FundraisingEventMapper eventMapper;

    @InjectMocks
    private FundraisingEventService eventService;

    @Test
    public void createEventShouldCreateSuccessfullyTest(){
        FundraisingEventDtoRequest eventDtoRequest =
                FundraisingEventDtoRequest.builder()
                    .name("NAME")
                .build();
        FundraisingEvent event =
                FundraisingEvent.builder()
                    .name("NAME")
                .build();
        FundraisingEventDtoResponse eventDtoResponse =
                FundraisingEventDtoResponse.builder()
                    .name("NAME")
                .build();

        Mockito.when(eventMapper.eventDtoRequestToEvent(eventDtoRequest)).thenReturn(event);
        Mockito.when(eventRepository.save(event)).thenReturn(event);
        Mockito.when(eventMapper.eventToEventDtoResponse(event)).thenReturn(eventDtoResponse);

        FundraisingEventDtoResponse result = eventService.create(eventDtoRequest);

        assertNotNull(result);
        assertEquals("NAME", result.name());
    }

    @Test
    public void createEventThrowIllegalArgumentExceptionTest(){
        FundraisingEventDtoRequest eventDtoRequest =
                FundraisingEventDtoRequest.builder()
                        .name("")
                        .build();
        assertThrows(
                IllegalArgumentException.class,
                () -> eventService.create(eventDtoRequest)
        );
    }

    @Test
    public void createEventWithDuplicateNameTest(){
        String duplicateName = "Duplicate-Name";
        FundraisingEventDtoRequest eventDtoRequest =
                FundraisingEventDtoRequest.builder()
                        .name(duplicateName)
                        .build();
        Optional<FundraisingEvent> event = Optional.ofNullable(FundraisingEvent.builder()
                .uuid(UUID.randomUUID())
                .name(duplicateName)
                .build()
        );

        Mockito.when(eventRepository.findByName(duplicateName)).thenReturn(event);

        assertThrows(
                EventDuplicateNameException.class,
                () -> eventService.create(eventDtoRequest)
        );
    }

    @Test
    public void transferTheSameCurrencySuccessfullyTest(){
        Double amount = 100.0;
        FundraisingEvent event = FundraisingEvent.builder()
                .uuid(UUID.randomUUID())
                .name("Test")
                .currencyCode("PLN")
                .build();
        CollectionBox collectionBox = CollectionBox.builder()
                .uuid(UUID.randomUUID())
                .event(event)
                .balance(new HashMap<>(){{put("PLN",amount);}})
                .build();

        Mockito.when(eventRepository.findById(event.getUuid()))
                .thenReturn(Optional.of(event));
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid()))
                .thenReturn(Optional.of(collectionBox));

        eventService.transfer(event.getUuid(), collectionBox.getUuid());
        Double balanceCollectionBox = collectionBox.getBalance().values().stream().mapToDouble(d -> d.doubleValue()).sum();

        assertEquals(amount, event.getAmount());
        assertEquals(0.0, balanceCollectionBox);
    }

    @Test
    public void transferTheSameCurrencyUnsuccessfullyTest(){
        Double amount = 250.0;
        FundraisingEvent event = FundraisingEvent.builder()
                .uuid(UUID.randomUUID())
                .name("Test")
                .currencyCode("PLN")
                .build();
        CollectionBox collectionBox = CollectionBox.builder()
                .uuid(UUID.randomUUID())
                .balance(new HashMap<>(){{put("PLN",amount);}})
                .build();

        Mockito.when(eventRepository.findById(event.getUuid()))
                .thenReturn(Optional.of(event));
        Mockito.when(collectionBoxRepository.findById(collectionBox.getUuid()))
                .thenReturn(Optional.of(collectionBox));

        eventService.transfer(event.getUuid(), collectionBox.getUuid());

        assertNotEquals(100.0, event.getAmount());
    }
}
