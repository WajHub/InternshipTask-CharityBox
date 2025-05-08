package pl.wajhub.server.mapper;

import org.mapstruct.Mapper;
import pl.wajhub.server.dto.request.FundraisingEventDtoRequest;
import pl.wajhub.server.dto.response.FundraisingEventDtoResponse;
import pl.wajhub.server.model.FundraisingEvent;

@Mapper(componentModel = "spring")
public interface FundraisingEventMapper {

    FundraisingEvent eventDtoRequestToEvent(FundraisingEventDtoRequest eventDtoRequest);

    FundraisingEventDtoResponse eventToEventDtoResponse(FundraisingEvent event);
}
