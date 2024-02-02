package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.CreditCard;
import com.vidasoft.magman.model.Subscriber;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@RequestScoped
@Path("subscription")
public class SubscriptionResource {

    @Inject
    PaymentService paymentService;

    @PUT
    @Transactional
    @Path("{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPaymentMethod(@PathParam("userId") Long userId, CreditCardDTO creditCardDTO) {
        if (userId < 1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (creditCardDTO.getType() == null || creditCardDTO.getNumber() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        CreditCard creditCard = new CreditCard(creditCardDTO.getNumber(), creditCardDTO.getType());
        return Subscriber.<Subscriber>findByIdOptional(userId)
                .stream().peek(s -> s.creditCard = creditCard)
                .findFirst().map(s -> Response.status(Response.Status.NO_CONTENT).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Path("{userId}")
    public Response chargeSubscriber(@PathParam("userId") Long userId) {
        if (userId < 1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Subscriber.<Subscriber>findByIdOptional(userId)
                .map(paymentService::chargeSubscriber)
                .map(result -> result? Response.status(Response.Status.NO_CONTENT).build() : Response.status(Response.Status.NOT_ACCEPTABLE).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
