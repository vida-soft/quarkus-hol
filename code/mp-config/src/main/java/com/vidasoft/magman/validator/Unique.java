package com.vidasoft.magman.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = {UniqueUserValidator.class})
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
public @interface Unique {

    String message() default "The entered username must be unique.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
