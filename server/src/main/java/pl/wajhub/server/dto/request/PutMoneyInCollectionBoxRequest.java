package pl.wajhub.server.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import pl.wajhub.server.validation.CurrencyCodeConstraint;

@Builder
public record PutMoneyInCollectionBoxRequest(
        @Schema(name = "CurrencyCode", example = "PLN", description = "Currency code according to ISO 4217")
        @CurrencyCodeConstraint String currencyCode,
        Double amount) {
}
