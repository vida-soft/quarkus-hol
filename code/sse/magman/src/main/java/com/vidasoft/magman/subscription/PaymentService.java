package com.vidasoft.magman.subscription;

import com.vidasoft.magman.messaging.KafkaMessageService;
import com.vidasoft.magman.messaging.PaymentPayload;
import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.model.Subscription;
import com.vidasoft.magman.model.SubscriptionStatus;
import com.vidasoft.magman.spendpal.ConfirmationDTO;
import com.vidasoft.magman.spendpal.SpendPalClient;
import com.vidasoft.magman.spendpal.SpendPalException;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class PaymentService {

    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());

    @Inject
    @ChargedSubscriber
    Event<SubscriberChargedPayload> onSubscriberCharged;

    @Inject
    @RestClient
    SpendPalClient spendPalClient;

    @Inject
    KafkaMessageService kafkaMessageService;

    @Transactional
    public boolean chargeSubscriber(Subscriber subscriber) throws SpendPalException {
        subscriber = Subscriber.getEntityManager().merge(subscriber); //We make sure that the subscriber instance is attached to the entity manager.
        failPreviousSubscriptionAttempt(subscriber);

        Subscription subscription = new Subscription(subscriber);
        subscription.persist();

        if (subscriber.creditCard != null) {
            CreditCardDTO creditCardDTO = new CreditCardDTO(subscriber.creditCard);
            try {
                kafkaMessageService.sendPaymentsMessage(subscriber.id, new PaymentPayload(subscriber.userName, creditCardDTO));
                return true;
            } catch (Exception e) {
                LOGGER.severe(e.getMessage());
                return chargeSubscriberThroughRest(subscriber);
            }
        } else {
            subscription.status = SubscriptionStatus.FAILED;
            subscription.completed = LocalDateTime.now();
            return false;
        }

    }

    private boolean chargeSubscriberThroughRest(Subscriber subscriber) throws SpendPalException {
        ConfirmationDTO paymentResult = spendPalClient.chargeCustomer(new CreditCardDTO(subscriber.creditCard));
        LOGGER.log(Level.INFO, "Charging subscriber with id: {0}  and card type {1} of number: {2}",
                new Object[]{subscriber.id, subscriber.creditCard.creditCardType, subscriber.creditCard.number});

        if (paymentResult.getSuccess()) {
            LOGGER.log(Level.INFO, "Successfully charged customer with id: {0}  and card type {1} of number: {2}",
                    new Object[]{subscriber.id, subscriber.creditCard.creditCardType, subscriber.creditCard.number});
            onSubscriberCharged.fire(new SubscriberChargedPayload(subscriber, paymentResult));
            return true;

        } else {
            LOGGER.log(Level.WARNING, "Unable to charge customer with id: {0}  and card type {1} of number: {2}",
                    new Object[]{subscriber.id, subscriber.creditCard.creditCardType, subscriber.creditCard.number});
            // Will probably email the customer, or, most likely, call the police.
            // Failed payments feel like federal crime after all ¯\_(ツ)_/¯

            return false;
        }
    }

    private void failPreviousSubscriptionAttempt(Subscriber subscriber) {
        Subscription.findLastPendingSubscription(subscriber)
                .ifPresent(s -> {
                    s.status = SubscriptionStatus.FAILED;
                    s.completed = LocalDateTime.now();
                });
    }
}
