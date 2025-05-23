package pl.wajhub.server.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wajhub.server.dto.request.PutMoneyInCollectionBoxRequest;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.service.CollectionBoxService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CollectionBoxController {

    private final CollectionBoxService collectionBoxService;

    @Autowired
    public CollectionBoxController(CollectionBoxService collectionBoxService) {
        this.collectionBoxService = collectionBoxService;
    }

    @GetMapping("/collections")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CollectionBoxDtoResponse>> getCollections(){
        List<CollectionBoxDtoResponse> boxes = collectionBoxService.getAll();
        return new ResponseEntity<>(boxes, HttpStatus.OK);
    }

    @PostMapping("/collections")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CollectionBoxDtoResponse> createCollection(){
        CollectionBoxDtoResponse collectionBox = collectionBoxService.create();
        return new ResponseEntity<>(collectionBox, HttpStatus.CREATED);
    }

    @PutMapping("/collections/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CollectionBoxDtoResponse> createCollection(
            @PathVariable UUID uuid
    ){
        CollectionBoxDtoResponse collectionBox = collectionBoxService.create(uuid);
        return new ResponseEntity<>(collectionBox, HttpStatus.CREATED);
    }

    @PatchMapping("/events/{eventUuid}/collections/{collectionUuid}/register")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CollectionBoxDtoResponse> registerCollection(
            @PathVariable UUID eventUuid,
            @PathVariable UUID collectionUuid
            ) {
        CollectionBoxDtoResponse collectionBox = collectionBoxService.register(eventUuid,collectionUuid);
        return new ResponseEntity<>(collectionBox, HttpStatus.OK);
    }

    @DeleteMapping("/collections/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unregisterCollection(@PathVariable UUID uuid) {
        collectionBoxService.unregister(uuid);
    }

    @PatchMapping("/collections/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CollectionBoxDtoResponse> putMoney(
            @PathVariable UUID uuid,
            @Valid @RequestBody PutMoneyInCollectionBoxRequest money
            ) {
        CollectionBoxDtoResponse collectionBox = collectionBoxService.putMoney(uuid, money);
        return new ResponseEntity<>(collectionBox, HttpStatus.OK);
    }

    @PatchMapping("collections/{uuid}/transfer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CollectionBoxDtoResponse> transferMoney(
            @PathVariable UUID uuid
    ) {
        CollectionBoxDtoResponse collectionBox = collectionBoxService.transfer(uuid);
        return new ResponseEntity<>(collectionBox, HttpStatus.OK);
    }

}
