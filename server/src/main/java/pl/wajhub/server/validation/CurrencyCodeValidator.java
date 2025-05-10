package pl.wajhub.server.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;
import java.util.Objects;

public class CurrencyCodeValidator  implements ConstraintValidator<CurrencyCodeConstraint, String> {

    @Override
    public void initialize(CurrencyCodeConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext constraintValidatorContext) {
        var test = Currency.getAvailableCurrencies()
                .stream()
                .anyMatch(currency->
                        Objects.equals(currency.getCurrencyCode(), code)
                );
        System.out.println(test);
        return true;
    }
}
