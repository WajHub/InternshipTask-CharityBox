package pl.wajhub.server.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.wajhub.server.exception.CurrencyCodeException;

import java.util.Currency;
import java.util.Objects;

public class CurrencyCodeValidator  implements ConstraintValidator<CurrencyCodeConstraint, String> {

    @Override
    public void initialize(CurrencyCodeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {
        var isValid =  Currency.getAvailableCurrencies()
                .stream()
                .anyMatch(currency->
                        Objects.equals(currency.getCurrencyCode(), code)
                );
        if(!isValid) throw new CurrencyCodeException(code);
        return true;
    }
}
