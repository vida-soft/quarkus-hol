package com.vidasoft.magman.validator;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.validation.Validation;
import javax.validation.Validator;
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
