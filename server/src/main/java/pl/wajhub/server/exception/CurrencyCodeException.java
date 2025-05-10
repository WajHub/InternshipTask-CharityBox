package pl.wajhub.server.exception;

public class CurrencyCodeException extends RuntimeException {
    public CurrencyCodeException(String currencyCode) {

        super("Currency code: "+currencyCode+" is not available in ISO 4217!");
    }
}
