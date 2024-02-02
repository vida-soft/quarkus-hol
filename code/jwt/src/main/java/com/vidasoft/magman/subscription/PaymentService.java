package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.spendpal.ConfirmationDTO;
import com.vidasoft.magman.spendpal.SpendPalClient;
import com.vidasoft.magman.spendpal.SpendPalException;
import org.eclipse.microprofile.rest.client.inject.RestClient;

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

    @Inject
    @RestClient
    SpendPalClient spendPalClient;

    boolean chargeSubscriber(Subscriber subscriber) throws SpendPalException {
        if (subscriber.creditCard != null) {
            ConfirmationDTO paymentResult = spendPalClient.chargeCustomer(new CreditCardDTO(subscriber.creditCard));
            LOGGER.log(Level.INFO, "Charging subscriber with id: {0}  and card type {1} of number: {2}",
                    new Object[]{subscriber.id, subscriber.creditCard.creditCardType, subscriber.creditCard.number});

            if (paymentResult.getSuccess()) {
                LOGGER.log(Level.INFO, "Successfully charged customer with id: {0}  and card type {1} of number: {2}",
                        new Object[]{subscriber.id, subscriber.creditCard.creditCardType, subscriber.creditCard.number});
                onSubscriberCharged.fire(subscriber);
                return true;

            } else {
                LOGGER.log(Level.WARNING, "Unable to charge customer with id: {0}  and card type {1} of number: {2}",
                        new Object[]{subscriber.id, subscriber.creditCard.creditCardType, subscriber.creditCard.number});
                // Will probably email the customer, or, most likely, call the police.
                // Failed payments feel like federal crime after all ¯\_(ツ)_/¯

                return false;
            }

        }

        return false;
    }

}
