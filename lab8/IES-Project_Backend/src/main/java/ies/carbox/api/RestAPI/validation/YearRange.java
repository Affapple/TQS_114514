package ies.carbox.api.RestAPI.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = YearRangeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface YearRange {
    String message() default "Year must be between 1950 and the current year";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
