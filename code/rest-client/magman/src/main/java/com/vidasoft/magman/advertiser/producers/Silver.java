package com.vidasoft.magman.advertiser.producers;

import com.vidasoft.magman.model.SponsorPackage;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;
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
public @interface Silver {
    SponsorPackage SPONSOR_PACKAGE = SponsorPackage.SILVER;

    @Nonbinding
    int limit() default 0;
}
