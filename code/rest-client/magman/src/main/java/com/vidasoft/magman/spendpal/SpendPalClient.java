package com.vidasoft.magman.spendpal;

import com.vidasoft.magman.subscription.CreditCardDTO;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
