package com.vidasoft.magman.validator;

import com.vidasoft.magman.model.User;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUserValidator implements ConstraintValidator<Unique, String> {
    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return User.find("userName", username).firstResultOptional().isEmpty();
    }
}
