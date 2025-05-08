package pl.wajhub.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wajhub.server.dto.response.CollectionBoxDtoResponse;
import pl.wajhub.server.service.CollectionBoxService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class CollectionBoxController {

    private final CollectionBoxService collectionBoxService;

    @Autowired
    public CollectionBoxController(CollectionBoxService collectionBoxService) {
        this.collectionBoxService = collectionBoxService;
    }


    @PostMapping("/events/{uuid}/collections")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CollectionBoxDtoResponse> createCollection(@PathVariable UUID uuid){
        CollectionBoxDtoResponse collectionBox = collectionBoxService.create(uuid);
        return new ResponseEntity<>(collectionBox, HttpStatus.CREATED);
    }

}
