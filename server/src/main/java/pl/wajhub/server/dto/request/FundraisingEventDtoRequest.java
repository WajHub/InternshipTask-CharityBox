package pl.wajhub.server.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import pl.wajhub.server.validation.CurrencyCodeConstraint;

@Builder
public record FundraisingEventDtoRequest(
        @NotEmpty String name,
        @Schema(name = "currencyCode", example = "PLN", description = "Currency code according to ISO 4217")
        @CurrencyCodeConstraint String currencyCode) {
}
