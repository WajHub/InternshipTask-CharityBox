package pl.wajhub.server.dto.request;

import lombok.Builder;
import pl.wajhub.server.model.MyCurrency;

@Builder
public record TransferMoneyToCollectionBoxRequest(MyCurrency currency, Double amount) {
}
