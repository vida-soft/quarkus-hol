package com.vidasoft.magman.advertiser.producers;

import com.vidasoft.magman.model.SponsorPackage;

import jakarta.enterprise.util.Nonbinding;
import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Qualifier
@Retention(RUNTIME)
@Target({METHOD, FIELD, PARAMETER, TYPE})
public @interface Gold {

    SponsorPackage SPONSOR_PACKAGE = SponsorPackage.GOLD;

    @Nonbinding
    int limit() default 0;

}
