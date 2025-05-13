package pl.wajhub.server.initializer;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.request.PutMoneyInCollectionBoxRequest;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.model.FundraisingEvent;
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

        collectionBoxService.putMoney(
                collectionBox.uuid(),
                PutMoneyInCollectionBoxRequest.builder().currencyCode("EUR").amount(100.0).build()
        );
        collectionBoxService.putMoney(
                collectionBox.uuid(),
                PutMoneyInCollectionBoxRequest.builder().currencyCode("USD").amount(50.2).build()
        );

        FundraisingEventDtoResponse event2 =
                eventService.create(
                        FundraisingEventDtoRequest.builder()
                                .name("All for hope")
                                .currencyCode("GBP")
                                .build(),
                        UUID.fromString("56eb1354-a780-446f-beb9-705602f25104")
                );

        CollectionBoxDtoResponse notRegisteredCollection = collectionBoxService.
                create(UUID.fromString("5a65a78b-e765-4b26-92ea-4903124ae19c"));


        FundraisingEventDtoResponse event3 =
                eventService.create(
                        FundraisingEventDtoRequest.builder()
                              .name("Wielka Orkiestra Świątecznej Pomocy")
                              .currencyCode("PLN")
                              .build(),
                        UUID.fromString("06c763c3-23c7-4d72-b46c-3d59c564656c")
                );

        CollectionBoxDtoResponse collectionBox2_1 = collectionBoxService.
                create(UUID.fromString("963b0f2c-1baf-4d41-b277-f4ab2c2e3875"));

        CollectionBoxDtoResponse collectionBox2_2 = collectionBoxService.
                create(UUID.fromString("e4df6957-e525-42c5-9064-9831ed7cd0e7"));

        CollectionBoxDtoResponse collectionBox2_3 = collectionBoxService.
                create(UUID.fromString("cd40360d-2288-4502-96d9-fc4a190eb2e2"));

        CollectionBoxDtoResponse collectionBox3_1 = collectionBoxService.
                create(UUID.fromString("cfa73bc4-5196-4a67-bec8-20181415f21b"));

        CollectionBoxDtoResponse collectionBox4 = collectionBoxService.
                create(UUID.fromString("e329a2ca-d512-422d-a21c-9bbcbe034ef9"));

        collectionBoxService.register(event2.uuid(), collectionBox2_1.uuid());
        collectionBoxService.putMoney(
                collectionBox2_1.uuid(),
                PutMoneyInCollectionBoxRequest.builder().currencyCode("BGN").amount(100.0).build()
        );
        collectionBoxService.putMoney(
                collectionBox2_1.uuid(),
                PutMoneyInCollectionBoxRequest.builder().currencyCode("CZK").amount(2200.50).build()
        );
        collectionBoxService.putMoney(
                collectionBox2_1.uuid(),
                PutMoneyInCollectionBoxRequest.builder().currencyCode("CNY").amount(20.75).build()
        );

        collectionBoxService.register(event2.uuid(), collectionBox2_2.uuid());
        collectionBoxService.putMoney(
                collectionBox2_2.uuid(),
                PutMoneyInCollectionBoxRequest.builder().currencyCode("USD").amount(53.50).build()
        );

        collectionBoxService.register(event2.uuid(), collectionBox2_3.uuid());
        collectionBoxService.putMoney(
                collectionBox2_3.uuid(),
                PutMoneyInCollectionBoxRequest.builder().currencyCode("PLN").amount(21.37).build()
        );
        collectionBoxService.register(event3.uuid(), collectionBox3_1.uuid());

        collectionBoxService.putMoney(
                collectionBox2_3.uuid(),
                PutMoneyInCollectionBoxRequest.builder().currencyCode("AUD").amount(1.11).build()
        );

    }
}
