package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.CreditCard;
import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.model.User;
import com.vidasoft.magman.security.LoggedUser;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@RequestScoped
public class SubscriptionResource implements SubscriptionAPI {

    @Inject
    PaymentService paymentService;

    @Inject
    @LoggedUser
    User loggedUser;

    @Override
    @Transactional
    public Response addPaymentMethod(Long userId, CreditCardDTO creditCardDTO) {
        CreditCard creditCard = new CreditCard(creditCardDTO.getNumber(), creditCardDTO.getType());
        return Subscriber.<Subscriber>findByIdOptional(userId)
                .stream().peek(s -> s.creditCard = creditCard)
                .findFirst().map(s -> Response.status(Response.Status.NO_CONTENT).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @Override
    public Response chargeSubscriber() {
        if (loggedUser instanceof Subscriber subscriber) {
            var result = paymentService.chargeSubscriber(subscriber);
            return result ? Response.status(Response.Status.NO_CONTENT).build() :
                    Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }
}
