package ies.carbox.api.RestAPI.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Calendar;

public class YearRangeValidator implements ConstraintValidator<YearRange, Integer> {
    private static final int MIN_YEAR = 1950;

    @Override
    public boolean isValid(Integer year, ConstraintValidatorContext context) {
        if (year == null) {
            return false;
        }
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return year >= MIN_YEAR && year <= currentYear;
    }
}
