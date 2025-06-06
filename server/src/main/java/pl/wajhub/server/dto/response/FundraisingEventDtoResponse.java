package pl.wajhub.server.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import pl.wajhub.server.validation.CurrencyCodeConstraint;

import java.util.UUID;

@Builder
public record FundraisingEventDtoResponse(
        UUID uuid,
        String name,
        @Schema(name = "CurrencyCode", example = "PLN", description = "Currency code according to ISO 4217")
        @CurrencyCodeConstraint String currencyCode,
        Double balance) {
}
