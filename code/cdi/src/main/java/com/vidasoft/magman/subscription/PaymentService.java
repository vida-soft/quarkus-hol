package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.Subscriber;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class PaymentService {

    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());

    @Inject
    @ChargedSubscriber
    Event<Subscriber> onSubscriberCharged;

    boolean chargeSubscriber(Subscriber subscriber) {
        if (subscriber.creditCard != null) {
            LOGGER.log(Level.INFO, "Charging subscriber with id: {0}  and card type {1} of number: {2}",
                    new Object[]{subscriber.id, subscriber.creditCard.creditCardType, subscriber.creditCard.number});

            onSubscriberCharged.fire(subscriber);
            return true;
        }

        return false;
    }

}
