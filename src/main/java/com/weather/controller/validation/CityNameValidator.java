package com.weather.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class CityNameValidator implements ConstraintValidator<CityNameConstraint,String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String value = s.replaceAll("[^a-zA-Z0-9]","");
        boolean isValid = !StringUtils.isNumeric(value) && !StringUtils.isAllBlank(value);

        return isValid;
    }
}
