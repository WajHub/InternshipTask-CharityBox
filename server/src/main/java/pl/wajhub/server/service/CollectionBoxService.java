package pl.wajhub.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.exception.EventNotFoundException;
import pl.wajhub.server.mapper.CollectionBoxMapper;
import pl.wajhub.server.model.CollectionBox;
import pl.wajhub.server.repository.CollectionBoxRepository;
import pl.wajhub.server.repository.FundraisingEventRepository;

import java.util.UUID;

@Service
public class CollectionBoxService {

    private final CollectionBoxRepository collectionBoxRepository;
    private final FundraisingEventRepository eventRepository;
    private final CollectionBoxMapper collectionBoxMapper;

    @Autowired
    public CollectionBoxService(
            CollectionBoxRepository collectionBoxRepository,
            FundraisingEventRepository eventRepository,
            CollectionBoxMapper collectionBoxMapper) {
        this.collectionBoxRepository = collectionBoxRepository;
        this.eventRepository = eventRepository;
        this.collectionBoxMapper = collectionBoxMapper;
    }


    public CollectionBoxDtoResponse create(UUID event_uuid) {
        return
            eventRepository
            .findById(event_uuid)
            .map((event) -> {
                var collectionBox = CollectionBox.builder().event(event).build();
                var collectionBoxSaved = collectionBoxRepository.save(collectionBox);
                return collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBoxSaved);
            })
            .orElseThrow(() -> new EventNotFoundException(event_uuid));
    }
}
