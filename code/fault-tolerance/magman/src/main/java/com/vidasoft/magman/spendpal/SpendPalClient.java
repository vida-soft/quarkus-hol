package com.vidasoft.magman.spendpal;

import com.vidasoft.magman.subscription.CreditCardDTO;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("payment")
@RegisterProvider(SpendPalExceptionMapper.class)
@RegisterRestClient(configKey = "spendpal-client")
@RegisterClientHeaders(SpendPalHeaderFactory.class)
public interface SpendPalClient {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    ConfirmationDTO chargeCustomer(CreditCardDTO creditCardDTO) throws SpendPalException;

}
