package pl.wajhub.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.service.FundraisingEventService;

@RestController
@RequestMapping("/api/v1")
public class FundraisingEventController {

    private final FundraisingEventService eventService;

    @Autowired
    public FundraisingEventController(FundraisingEventService eventService) {
        this.eventService = eventService;
    }


    @PostMapping("/events")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FundraisingEventDtoResponse> createEvent(
            @RequestBody FundraisingEventDtoRequest eventDtoRequest
    ){
        if( eventDtoRequest == null || eventDtoRequest.name().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        FundraisingEventDtoResponse event = eventService.create(eventDtoRequest);
        return new ResponseEntity<>(event, HttpStatus.CREATED);
    }

}
