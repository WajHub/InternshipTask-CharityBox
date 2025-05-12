package pl.wajhub.server.service;

import org.junit.jupiter.api.BeforeEach;
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
import pl.wajhub.server.model.FundraisingEvent;
import pl.wajhub.server.repository.CollectionBoxRepository;
import pl.wajhub.server.repository.FundraisingEventRepository;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FundraisingEventServiceUnitTests {

    @Mock
    private FundraisingEventRepository eventRepository;
    @Mock
    private CollectionBoxRepository collectionBoxRepository;
    @Mock
    private FundraisingEventMapper eventMapper;
    @Mock
    private ExchangeService exchangeService;
    @InjectMocks
    private FundraisingEventService eventService;

    private String eventName;
    private String eventCurrencyCode;
    private FundraisingEventDtoRequest eventDtoRequest;
    private FundraisingEvent event;
    private FundraisingEventDtoResponse eventDtoResponse;

    @BeforeEach
    public void init(){
        eventName = "Charity One";
        eventCurrencyCode = "PLN";
        eventDtoRequest = FundraisingEventDtoRequest.builder()
                .name(eventName)
                .currencyCode(eventCurrencyCode)
                .build();
        event = FundraisingEvent.builder()
                .name(eventName)
                .currencyCode(eventCurrencyCode)
                .build();
        eventDtoResponse = FundraisingEventDtoResponse.builder()
                .name(eventName)
                .currencyCode(eventCurrencyCode)
                .build();
    }

    @Test
    public void create_SuccessfullyCreated_NewEvent(){
        Mockito.when(eventMapper.eventDtoRequestToEvent(eventDtoRequest)).thenReturn(event);
        Mockito.when(eventRepository.save(event)).thenReturn(event);
        Mockito.when(eventMapper.eventToEventDtoResponse(event)).thenReturn(eventDtoResponse);

        FundraisingEventDtoResponse result = eventService.create(eventDtoRequest);

        assertNotNull(result);
        assertEquals(eventDtoRequest.name(), result.name());
    }

    @Test
    public void create_ThrowIllegalArgumentException_EmptyName(){
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
    public void create_ThrowEventDuplicateNameException_DuplicateName(){
        FundraisingEventDtoRequest eventDtoRequest =
                FundraisingEventDtoRequest.builder()
                        .name(eventName)
                        .build();
        Optional<FundraisingEvent> event = Optional.ofNullable(FundraisingEvent.builder()
                .uuid(UUID.randomUUID())
                .name(eventName)
                .build()
        );
        Mockito.when(eventRepository.findByName(eventName)).thenReturn(event);

        assertThrows(
                EventDuplicateNameException.class,
                () -> eventService.create(eventDtoRequest)
        );
    }
}
