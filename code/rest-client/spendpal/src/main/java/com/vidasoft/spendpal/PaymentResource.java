package com.vidasoft.spendpal;

import io.quarkus.security.UnauthorizedException;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;

@RequestScoped
@Path("/payment")
public class PaymentResource {

    public static final String VERY_SECURE_TOKEN = "mostSecureTokenEver";

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ConfirmationDTO chargeCustomer(@Valid @NotNull CreditCardDTO creditCardDTO, @HeaderParam("authorization") String authorization) {
        if (VERY_SECURE_TOKEN.equals(authorization)) {
            return new ConfirmationDTO(true, LocalDateTime.now());
        } else {
            throw new UnauthorizedException();
        }
    }
}