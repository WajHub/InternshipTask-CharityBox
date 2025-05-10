package pl.wajhub.server.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.wajhub.server.exception.CurrencyCodeException;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyCodeValidationTests {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private CurrencyCodeValidator validator;

    @Test
    public void validateSuccessfullyTest(){
        assertTrue(validator.isValid("PLN", constraintValidatorContext));
        assertTrue(validator.isValid("EUR", constraintValidatorContext));
        assertTrue(validator.isValid("USD", constraintValidatorContext));
        assertTrue(validator.isValid("AWG", constraintValidatorContext));
        assertTrue(validator.isValid("CDF", constraintValidatorContext));
        assertTrue(validator.isValid("RWF", constraintValidatorContext));
        assertTrue(validator.isValid("EGP", constraintValidatorContext));
    }

    @Test
    public void throwCurrencyCodeException(){
        assertThrows(
                CurrencyCodeException.class,
                ()-> validator.isValid("FALSE", constraintValidatorContext)
        );
        assertThrows(
                CurrencyCodeException.class,
                ()-> validator.isValid("...", constraintValidatorContext)
        );
        assertThrows(
                CurrencyCodeException.class,
                ()-> validator.isValid("UDS", constraintValidatorContext)
        );
        assertThrows(
                CurrencyCodeException.class,
                ()-> validator.isValid("PL", constraintValidatorContext)
        );
        assertThrows(
                CurrencyCodeException.class,
                ()-> validator.isValid("P.N", constraintValidatorContext)
        );
        assertThrows(
                CurrencyCodeException.class,
                ()-> validator.isValid("", constraintValidatorContext)
        );
        assertThrows(
                CurrencyCodeException.class,
                ()-> validator.isValid("pln", constraintValidatorContext)
        );
    }

}
