package pl.wajhub.server.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CollectionBoxDtoResponse(UUID uuid) {
}
