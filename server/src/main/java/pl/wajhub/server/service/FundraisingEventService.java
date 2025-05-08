package pl.wajhub.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.mapper.FundraisingEventMapper;
import pl.wajhub.server.model.FundraisingEvent;
import pl.wajhub.server.repository.FundraisingEventRepository;

@Service
public class FundraisingEventService {

    private final FundraisingEventRepository eventRepository;
    private final FundraisingEventMapper mapper;

    @Autowired
    public FundraisingEventService(
            FundraisingEventRepository eventRepository,
            @Qualifier("fundraisingEventMapperImpl") FundraisingEventMapper mapper) {
        this.eventRepository = eventRepository;
        this.mapper = mapper;
    }

    public FundraisingEventDtoResponse create(FundraisingEventDtoRequest eventDtoRequest) {
        if (eventDtoRequest.name() == null || eventDtoRequest.name().isEmpty())
            throw new IllegalArgumentException("Event name must not be empty");

        FundraisingEvent event = eventRepository.save(
                mapper.eventDtoRequestToEvent(eventDtoRequest)
        );
        return mapper.eventToEventDtoResponse(event);
    }
}
