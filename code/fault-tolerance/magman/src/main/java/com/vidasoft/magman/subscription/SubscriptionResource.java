package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.CreditCard;
import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.model.User;
import com.vidasoft.magman.security.LoggedUser;
import com.vidasoft.magman.spendpal.SpendPalException;
import io.quarkus.narayana.jta.runtime.TransactionConfiguration;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.IOException;

@RequestScoped
@Authenticated
@Path("subscription")
public class SubscriptionResource {

    @Inject
    PaymentService paymentService;

    @Inject
    @LoggedUser
    User loggedUser;

    @PUT
    @Transactional
    @Path("{userId}")
    @RolesAllowed({Subscriber.ROLE_NAME})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPaymentMethod(@Positive @PathParam("userId") Long userId, @Valid @NotNull CreditCardDTO creditCardDTO) {
        CreditCard creditCard = new CreditCard(creditCardDTO.getNumber(), creditCardDTO.getType());
        return Subscriber.<Subscriber>findByIdOptional(userId)
                .stream().peek(s -> s.creditCard = creditCard)
                .findFirst().map(s -> Response.status(Response.Status.NO_CONTENT).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @RolesAllowed({Subscriber.ROLE_NAME})
    @Produces(MediaType.APPLICATION_JSON)
    public Response chargeSubscriber() {
        if (loggedUser instanceof Subscriber subscriber) {
            var result = paymentService.chargeSubscriber(subscriber);
            return result ? Response.status(Response.Status.NO_CONTENT).build() :
                    Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }
}
