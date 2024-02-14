package com.vidasoft.magman.security;

import com.vidasoft.magman.model.User;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
public class LoggedUserProducer {

    @Inject
    JsonWebToken jwt;

    @Produces
    @Dependent
    @LoggedUser
    public User getLoggedUser() {
        String upn = jwt.getClaim("upn"); //We get the user id from here
        if (upn == null) {
            return null;
        }

        Long userId = Long.parseLong(jwt.getClaim("upn"));
        return User.findById(userId);
    }

}
