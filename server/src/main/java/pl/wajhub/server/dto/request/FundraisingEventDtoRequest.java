package pl.wajhub.server.dto.request;

import lombok.Builder;
import pl.wajhub.server.model.MyCurrency;

@Builder
public record FundraisingEventDtoRequest(String name, MyCurrency currency) {
}
