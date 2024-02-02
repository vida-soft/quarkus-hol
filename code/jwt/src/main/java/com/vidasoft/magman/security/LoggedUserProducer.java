package com.vidasoft.magman.security;

import com.vidasoft.magman.model.User;
import org.eclipse.microprofile.jwt.JsonWebToken;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;

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
