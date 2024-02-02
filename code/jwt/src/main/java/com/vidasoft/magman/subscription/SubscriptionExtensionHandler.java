package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.Subscriber;
import io.smallrye.config.Priorities;
import jakarta.annotation.Priority;

import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubscriptionExtensionHandler {

    private static final Logger LOGGER = Logger.getLogger(SubscriptionExtensionHandler.class.getName());

    @Transactional
    @ActivateRequestContext
    public void observeSubscriptionExtension(@Priority(Priorities.APPLICATION + 2000) @Observes @ChargedSubscriber Subscriber subscriber) {
        subscriber.subscribedUntil = subscriber.subscribedUntil.plusYears(1);
        LOGGER.log(Level.INFO, "Extended subscription for user {0}, till {1}",
                List.of(subscriber.id, subscriber.subscribedUntil.toString()).toArray());
    }

    public void sendEmail(@Priority(Priorities.APPLICATION + 1000) @Observes @ChargedSubscriber Subscriber subscriber) {
        LOGGER.log(Level.INFO, "Sent email to subscriber {0}, about their subscription renewal.", subscriber.id);
    }

}
