package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.model.Subscription;
import com.vidasoft.magman.model.SubscriptionStatus;
import io.smallrye.config.Priorities;
import jakarta.annotation.Priority;

import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import jakarta.validation.Payload;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubscriptionExtensionHandler {

    private static final Logger LOGGER = Logger.getLogger(SubscriptionExtensionHandler.class.getName());

    @Transactional
    @ActivateRequestContext
    public void observeSubscriptionExtension(@Priority(Priorities.APPLICATION + 2000) @Observes @ChargedSubscriber SubscriberChargedPayload payload) {
        Subscriber subscriber = Subscriber.getEntityManager().merge(payload.subscriber()); //making sure that the subscriber entity is attached
        Subscription subscription = Subscription.findLastPendingSubscription(payload.subscriber())
                .orElse(new Subscription(payload.subscriber()));
        if (payload.confirmation().getSuccess()) {
            subscriber.subscribedUntil = subscriber.subscribedUntil.plusYears(1);
            subscription.status = SubscriptionStatus.VALID;
            LOGGER.log(Level.INFO, "Extended subscription for user {0}, till {1}",
                    List.of(subscriber.id, subscriber.subscribedUntil.toString()).toArray());
        } else {
            subscription.status = SubscriptionStatus.FAILED;
        }

        subscription.completed = payload.confirmation().getTimestamp();
    }

    public void sendEmail(@Priority(Priorities.APPLICATION + 1000) @Observes @ChargedSubscriber SubscriberChargedPayload payload) {
        LOGGER.log(Level.INFO, "Sent email to subscriber {0}, about their subscription renewal.", payload.subscriber().id);
    }

}
