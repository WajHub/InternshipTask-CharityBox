package pl.wajhub.server.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.service.FundraisingEventService;

@Component
public class DataInit {

    private final FundraisingEventService eventService;

    @Autowired
    public DataInit(FundraisingEventService eventService) {
        this.eventService = eventService;
    }

    @PostConstruct
    void initData() {
        eventService.create(
                FundraisingEventDtoRequest.builder()
                        .name("Test - Event")
                .build()
        );
    }
}
