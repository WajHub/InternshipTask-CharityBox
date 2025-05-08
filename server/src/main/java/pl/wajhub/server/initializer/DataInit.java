package pl.wajhub.server.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.model.FundraisingEvent;
import pl.wajhub.server.service.CollectionBoxService;
import pl.wajhub.server.service.FundraisingEventService;

@Component
public class DataInit {

    private final FundraisingEventService eventService;
    private final CollectionBoxService collectionBoxService;

    @Autowired
    public DataInit(FundraisingEventService eventService, CollectionBoxService collectionBoxService) {
        this.eventService = eventService;
        this.collectionBoxService = collectionBoxService;
    }

    @PostConstruct
    void initData() {
        FundraisingEventDtoResponse event =
            eventService.create(
                FundraisingEventDtoRequest.builder()
                        .name("Test - Event")
                .build()
            );
        collectionBoxService.create(event.uuid());
    }
}
