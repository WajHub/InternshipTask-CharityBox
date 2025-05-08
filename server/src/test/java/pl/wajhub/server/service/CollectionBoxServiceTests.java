package pl.wajhub.server.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.exception.EventNotFoundException;
import pl.wajhub.server.mapper.CollectionBoxMapper;
import pl.wajhub.server.model.CollectionBox;
import pl.wajhub.server.model.FundraisingEvent;
import pl.wajhub.server.repository.CollectionBoxRepository;
import pl.wajhub.server.repository.FundraisingEventRepository;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CollectionBoxServiceTests {

    @Mock
    private FundraisingEventRepository eventRepository;

    @Mock
    private CollectionBoxRepository collectionBoxRepository;

    @Mock
    private  CollectionBoxMapper collectionBoxMapper;

    @InjectMocks
    private CollectionBoxService collectionBoxService;

    @Test
    void createSuccessfullyTest() {
        UUID uuid = UUID.randomUUID();
        FundraisingEvent event = FundraisingEvent.builder()
                .uuid(uuid)
                .name("NAME")
                .build();

        CollectionBox savedBox = CollectionBox.builder()
                .uuid(UUID.randomUUID())
                .event(event)
                .build();

        CollectionBoxDtoResponse dtoResponse = CollectionBoxDtoResponse.builder()
                .uuid(savedBox.getUuid())
                .build();

        Mockito.when(eventRepository.findById(uuid)).thenReturn(Optional.of(event));
        Mockito.when(collectionBoxRepository.save(Mockito.any())).thenReturn(savedBox);
        Mockito.when(collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(savedBox)).thenReturn(dtoResponse);

        CollectionBoxDtoResponse result = collectionBoxService.create(uuid);

        assertNotNull(result);
        assertEquals(savedBox.getUuid(), result.uuid());
    }

    @Test
    void createThrowsEventNotFoundExceptionWhenEventDoesNotExist() {
        UUID uuid = UUID.randomUUID();

        Mockito.when(eventRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> collectionBoxService.create(uuid));
    }



}