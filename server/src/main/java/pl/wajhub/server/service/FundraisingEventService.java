package pl.wajhub.server.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.exception.CollectionBoxNotFoundException;
import pl.wajhub.server.exception.EventDuplicateNameException;
import pl.wajhub.server.exception.EventNotFoundException;
import pl.wajhub.server.mapper.FundraisingEventMapper;
import pl.wajhub.server.model.FundraisingEvent;
import pl.wajhub.server.repository.CollectionBoxRepository;
import pl.wajhub.server.repository.FundraisingEventRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FundraisingEventService {

    private final FundraisingEventRepository eventRepository;
    private final CollectionBoxRepository collectionBoxRepository;
    private final ExchangeService exchangeService;
    private final FundraisingEventMapper mapper;

    @Autowired
    public FundraisingEventService(
            FundraisingEventRepository eventRepository,
            CollectionBoxRepository collectionBoxRepository,
            ExchangeService exchangeService,
            @Qualifier("fundraisingEventMapperImpl") FundraisingEventMapper mapper) {
        this.eventRepository = eventRepository;
        this.collectionBoxRepository = collectionBoxRepository;
        this.exchangeService = exchangeService;
        this.mapper = mapper;
    }

    public List<FundraisingEventDtoResponse> getAll() {
        return
            eventRepository.findAll().stream()
            .map(mapper::eventToEventDtoResponse)
            .toList();
    }

    public FundraisingEventDtoResponse create(@Valid FundraisingEventDtoRequest eventDtoRequest) {
        if (eventDtoRequest.name() == null || eventDtoRequest.name().isEmpty())
            throw new IllegalArgumentException("Event name must not be empty");
        if(eventRepository.findByName(eventDtoRequest.name()).isPresent())
            throw new EventDuplicateNameException(eventDtoRequest.name());
        var event =  mapper.eventDtoRequestToEvent(eventDtoRequest);
        var eventSaved = eventRepository.save(event);
        return mapper.eventToEventDtoResponse(eventSaved);
    }

    public FundraisingEventDtoResponse create(@Valid FundraisingEventDtoRequest eventDtoRequest, UUID uuid) {
        if (eventDtoRequest.name() == null || eventDtoRequest.name().isEmpty())
            throw new IllegalArgumentException("Event name must not be empty");
        if(eventRepository.findByName(eventDtoRequest.name()).isPresent())
            throw new EventDuplicateNameException(eventDtoRequest.name());
        var eventOptional = eventRepository.findById(uuid);
        if (eventOptional.isPresent())
            return mapper.eventToEventDtoResponse(eventOptional.get());
        var event =  mapper.eventDtoRequestToEvent(eventDtoRequest);
        var eventSaved = eventRepository.save(event);
        return mapper.eventToEventDtoResponse(eventSaved);
    }

    @Transactional
    public FundraisingEventDtoResponse transfer(UUID eventUuid, UUID collectionUuid) {
        var event = eventRepository.findById(eventUuid)
                .orElseThrow(() -> new EventNotFoundException(eventUuid));
        var collectionBox = collectionBoxRepository.findById(collectionUuid)
                .orElseThrow(() -> new CollectionBoxNotFoundException(collectionUuid));

        collectionBox.getBalance()
            .forEach((currency, balance )->
                    handleTransferMoney(currency, balance, event)
            );
        collectionBox.setBalance(new HashMap<>());
        return mapper.eventToEventDtoResponse(event);
    }

    private void handleTransferMoney(String currencyCode, Double balance, FundraisingEvent event) {
        if(Objects.equals(currencyCode, event.getCurrencyCode())) {
            event.setAmount(event.getAmount() + balance);
            return ;
        }
        try {
            Double value = exchangeService.exchange(currencyCode, event.getCurrencyCode(), balance);
            event.setAmount(event.getAmount()+value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
