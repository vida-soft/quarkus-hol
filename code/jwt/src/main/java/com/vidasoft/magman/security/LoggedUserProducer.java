package com.vidasoft.magman.security;

import com.vidasoft.magman.model.User;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@RequestScoped
public class LoggedUserProducer {

    @Inject
    JsonWebToken jwt;

    @Produces
    @Dependent
    @LoggedUser
    private User getLoggedUser() {
        Long userId = Long.parseLong(jwt.getClaim("upn"));
        return User.findById(userId);
    }

}
