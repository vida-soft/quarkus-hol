package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.CreditCard;
import com.vidasoft.magman.model.Subscriber;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("subscription")
public class SubscriptionResource {

    @Inject
    PaymentService paymentService;

    @PUT
    @Transactional
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPaymentMethod(@Positive @PathParam("userId") Long userId, @Valid @NotNull CreditCardDTO creditCardDTO) {
        CreditCard creditCard = new CreditCard(creditCardDTO.getNumber(), creditCardDTO.getType());
        return Subscriber.<Subscriber>findByIdOptional(userId)
                .stream().peek(s -> s.creditCard = creditCard)
                .findFirst().map(s -> Response.status(Response.Status.NO_CONTENT).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("{userId}")
    public Response chargeSubscriber(@Positive @PathParam("userId") Long userId) {
        return Subscriber.<Subscriber>findByIdOptional(userId)
                .map(paymentService::chargeSubscriber)
                .map(result -> result ? Response.status(Response.Status.NO_CONTENT).build() : Response.status(Response.Status.NOT_ACCEPTABLE).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
