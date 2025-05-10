package pl.wajhub.server.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CurrencyCodeValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CurrencyCodeConstraint {
    String message() default "Invalid currency code!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
