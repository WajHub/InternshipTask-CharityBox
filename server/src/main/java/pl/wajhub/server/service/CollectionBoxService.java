package pl.wajhub.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wajhub.server.dto.request.TransferMoneyToCollectionBoxRequest;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.exception.CollectionBoxNotFoundException;
import pl.wajhub.server.exception.EventNotFoundException;
import pl.wajhub.server.exception.IncorrectMoneyValueException;
import pl.wajhub.server.mapper.CollectionBoxMapper;
import pl.wajhub.server.model.CollectionBox;
import pl.wajhub.server.model.FundraisingEvent;
import pl.wajhub.server.repository.CollectionBoxRepository;
import pl.wajhub.server.repository.FundraisingEventRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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

    public List<CollectionBoxDtoResponse> getAll() {
        return
            collectionBoxRepository.findAll().stream()
                .map(collectionBoxMapper::collectionBoxToCollectionBoxDtoResponse)
            .toList();
    }

    public CollectionBoxDtoResponse create() {
        var collection = CollectionBox.builder().balance(new HashMap<>()).build();
        var collectionBoxSaved = collectionBoxRepository.save(collection);
        return collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBoxSaved);
    }

    public CollectionBoxDtoResponse create(UUID uuid) {
        var collectionBoxOptional = collectionBoxRepository.findById(uuid);
        if (collectionBoxOptional.isPresent())
            return collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBoxOptional.get());
        var collection = CollectionBox.builder()
                    .uuid(uuid)
                    .balance(new HashMap<>())
                .build();
        var collectionBoxSaved = collectionBoxRepository.save(collection);
        return collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBoxSaved);
    }

    public CollectionBoxDtoResponse register(UUID eventUuid, UUID collectionUuid) {
        FundraisingEvent event = eventRepository.findById(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));
        CollectionBox collectionBox =
                collectionBoxRepository.findById(collectionUuid)
                .orElseThrow(() -> new CollectionBoxNotFoundException(collectionUuid));
        collectionBox.setEvent(event);
        collectionBoxRepository.save(collectionBox);
        return collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBox);
    }

    public CollectionBoxDtoResponse unregister(UUID uuid) {
        CollectionBox collectionBox =
                collectionBoxRepository.findById(uuid)
                .orElseThrow(() -> new CollectionBoxNotFoundException(uuid));
        collectionBox.setEvent(null);
        collectionBox.getBalance().replaceAll((k, v) -> 0.0);
        collectionBoxRepository.save(collectionBox);
        return collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBox);
    }

    @Transactional
    public CollectionBoxDtoResponse transfer(
            UUID uuid,
            TransferMoneyToCollectionBoxRequest money) {
        if(money.amount()<=0.0)
            throw new IncorrectMoneyValueException(money.amount());
        CollectionBox collectionBox = collectionBoxRepository.findById(uuid)
                .orElseThrow(() -> new CollectionBoxNotFoundException(uuid));
        Double value = collectionBox.getBalance().get(money.currencyCode());
        collectionBox.getBalance().replace(money.currencyCode(), money.amount()+value);
        var collectionBoxSaved = collectionBoxRepository.save(collectionBox);
        return collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBoxSaved);
    }
}
