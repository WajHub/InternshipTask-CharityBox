package pl.wajhub.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.mapper.FundraisingEventMapper;
import pl.wajhub.server.model.FundraisingEvent;
import pl.wajhub.server.repository.FundraisingEventRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FundraisingEventServiceTests {

    @Mock
    private FundraisingEventRepository eventRepository;

    @Mock
    private FundraisingEventMapper eventMapper;

    @InjectMocks
    private FundraisingEventService eventService;

    @Test
    public void saveEventTest(){
        // Arrange
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

        // Act
        FundraisingEventDtoResponse result = eventService.create(eventDtoRequest);

        assertNotNull(result);
        assertEquals("NAME", result.name());


    }

    @Test
    public void saveEventEmptyNameTest(){
        // Arrange
        FundraisingEventDtoRequest eventDtoRequest =
                FundraisingEventDtoRequest.builder()
                        .name("")
                        .build();
        // Assert
        assertThrows(
                IllegalArgumentException.class,
                () -> eventService.create(eventDtoRequest)
        );
    }
}
