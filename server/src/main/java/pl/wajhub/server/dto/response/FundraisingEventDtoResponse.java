package pl.wajhub.server.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record FundraisingEventDtoResponse(UUID uuid, String name) {
}
