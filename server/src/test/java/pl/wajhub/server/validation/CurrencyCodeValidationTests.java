package pl.wajhub.server.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    public void throwMethodArgumentNotValidException(){
        assertFalse(validator.isValid("FALSE", constraintValidatorContext));
        assertFalse(validator.isValid("...", constraintValidatorContext));
        assertFalse(validator.isValid("UDS", constraintValidatorContext));
        assertFalse(validator.isValid("PL", constraintValidatorContext));
        assertFalse(validator.isValid("P.N", constraintValidatorContext));
        assertFalse(validator.isValid("", constraintValidatorContext));
        assertFalse(validator.isValid("pln", constraintValidatorContext));
    }

}
