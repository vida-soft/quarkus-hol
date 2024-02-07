package com.vidasoft.spendpal;

import com.vidasoft.spendpal.dto.ConfirmationDTO;
import com.vidasoft.spendpal.dto.CreditCardDTO;
import io.quarkus.security.UnauthorizedException;

import jakarta.enterprise.context.RequestScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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