package pl.wajhub.server.dto.response;

import lombok.Builder;
import pl.wajhub.server.model.MyCurrency;

import java.util.UUID;

@Builder
public record FundraisingEventDtoResponse(UUID uuid, String name, MyCurrency currency, Double amount) {
}
