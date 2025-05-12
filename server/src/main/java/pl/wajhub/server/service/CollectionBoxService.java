package pl.wajhub.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class CollectionBoxService {

    private final ExchangeService exchangeService;
    private final CollectionBoxRepository collectionBoxRepository;
    private final FundraisingEventRepository eventRepository;
    private final CollectionBoxMapper collectionBoxMapper;

    @Autowired
    public CollectionBoxService(
            ExchangeService exchangeService,
            CollectionBoxRepository collectionBoxRepository,
            FundraisingEventRepository eventRepository,
            CollectionBoxMapper collectionBoxMapper) {
        this.exchangeService = exchangeService;
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
        if(!collectionBox.isEmpty())
            throw new CollectionBoxIsNotEmptyException();
        if(collectionBox.getEvent()!=null)
            throw new CollectionBoxIsAlreadyRegisteredException("You can't register collection box, which is already in use!");
        collectionBox.setEvent(event);
        collectionBoxRepository.save(collectionBox);
        return collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBox);
    }

    public void unregister(UUID uuid) {
        CollectionBox collectionBox =
                collectionBoxRepository.findById(uuid)
                .orElseThrow(() -> new CollectionBoxNotFoundException(uuid));
        collectionBoxRepository.delete(collectionBox);
    }

    @Transactional
    public CollectionBoxDtoResponse putMoney(
            UUID uuid,
            PutMoneyInCollectionBoxRequest money) {
        if(money.amount()<=0.0)
            throw new IncorrectMoneyValueException(money.amount());
        CollectionBox collectionBox = collectionBoxRepository.findById(uuid)
                .orElseThrow(() -> new CollectionBoxNotFoundException(uuid));
        if(collectionBox.getEvent() == null )
            throw new CollectionBoxIsNotAssigned("You can't put money in not assigned Collection Box!");
        collectionBox.transfer(money.currencyCode(), money.amount());
        var collectionBoxSaved = collectionBoxRepository.save(collectionBox);
        return collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBoxSaved);
    }

    @Transactional
    public CollectionBoxDtoResponse transfer(UUID collectionUuid) {
        var collectionBox = collectionBoxRepository.findById(collectionUuid)
                .orElseThrow(() -> new CollectionBoxNotFoundException(collectionUuid));
        var event = collectionBox.getEvent() ;
        if(collectionBox.getEvent()==null)
            throw new CollectionBoxIsNotAssigned("You can't transfer money from not assigned collection box!");
        collectionBox.getBalance()
                .forEach((currency, balance )->
                        handleTransferMoney(currency, balance, event)
                );
        collectionBox.setBalance(new HashMap<>());
        return  collectionBoxMapper.collectionBoxToCollectionBoxDtoResponse(collectionBox);
    }

    private void handleTransferMoney(String currencyCode, Double balance, FundraisingEvent event) {
        if(Objects.equals(currencyCode, event.getCurrencyCode())) {
            event.setBalance(event.getBalance() + balance);
            return ;
        }
        try {
            Double rate = exchangeService.getRate(currencyCode, event.getCurrencyCode());
            event.setBalance(event.getBalance()+rate*balance);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
