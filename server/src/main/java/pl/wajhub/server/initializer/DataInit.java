package pl.wajhub.server.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.request.TransferMoneyToCollectionBoxRequest;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.service.CollectionBoxService;
import pl.wajhub.server.service.FundraisingEventService;

import java.util.UUID;

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
                        .name("Charity One")
                        .currencyCode("EUR")
                .build(),
                UUID.fromString("66eaa713-c6a8-47c6-98fa-da78bfab9376")
            );
        CollectionBoxDtoResponse collectionBox = collectionBoxService.
                create(UUID.fromString("be4c9355-bac8-4262-84f9-07cc1eb1a192"));
        collectionBoxService.register(event.uuid(), collectionBox.uuid());
        collectionBoxService.transfer(
                collectionBox.uuid(),
                TransferMoneyToCollectionBoxRequest.builder().currencyCode("EUR").amount(100.0).build()
        );
        collectionBoxService.transfer(
                collectionBox.uuid(),
                TransferMoneyToCollectionBoxRequest.builder().currencyCode("USD").amount(50.2).build()
        );

        FundraisingEventDtoResponse event2 =
                eventService.create(
                        FundraisingEventDtoRequest.builder()
                                .name("All for hope")
                                .currencyCode("GBP")
                                .build(),
                        UUID.fromString("56eb1354-a780-446f-beb9-705602f25104")
                );

        CollectionBoxDtoResponse collectionBox2 = collectionBoxService.
                create(UUID.fromString("5a65a78b-e765-4b26-92ea-4903124ae19c"));

    }
}
