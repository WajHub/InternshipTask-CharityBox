package pl.wajhub.server.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.service.FundraisingEventService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class FundraisingEventController {

    private final FundraisingEventService eventService;

    @Autowired
    public FundraisingEventController(
            FundraisingEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<FundraisingEventDtoResponse>> getCollections(){
        List<FundraisingEventDtoResponse> events = eventService.getAll();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FundraisingEventDtoResponse> createEvent(
            @Valid @RequestBody FundraisingEventDtoRequest eventDtoRequest
    ){
        FundraisingEventDtoResponse event = eventService.create(eventDtoRequest);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @PutMapping("/events/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FundraisingEventDtoResponse> createEvent(
            @Valid @RequestBody FundraisingEventDtoRequest eventDtoRequest,
            @PathVariable UUID uuid
    ){
        if( eventDtoRequest == null || eventDtoRequest.name().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        FundraisingEventDtoResponse event = eventService.create(eventDtoRequest, uuid);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

    @PatchMapping("events/{eventUuid}/collections/{collectionUuid}/transfer")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<FundraisingEventDtoResponse> transferMoney(
            @PathVariable UUID eventUuid,
            @PathVariable UUID collectionUuid
            ) {
        FundraisingEventDtoResponse event = eventService.transfer(eventUuid, collectionUuid);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

}
