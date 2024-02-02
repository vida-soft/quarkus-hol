package com.vidasoft.magman.validator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ValidationService {
    Validator validator;

    @PostConstruct
    void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public <T> List<ViolationMessage> validateObject(T objectToValidate) {
        return validator.validate(objectToValidate)
                .stream()
                .map(v -> new ViolationMessage(v.getPropertyPath().toString(), v.getMessage()))
                .collect(Collectors.toList());

    }
}
