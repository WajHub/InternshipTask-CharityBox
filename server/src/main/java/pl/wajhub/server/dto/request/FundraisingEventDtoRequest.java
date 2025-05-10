package pl.wajhub.server.dto.request;

import lombok.Builder;
import pl.wajhub.server.validation.CurrencyCodeConstraint;

@Builder
public record FundraisingEventDtoRequest(String name, @CurrencyCodeConstraint String currencyCode) {
}
