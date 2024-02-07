package com.vidasoft.magman.messaging;

import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.subscription.ChargedSubscriber;
import com.vidasoft.magman.subscription.SubscriberChargedPayload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.logging.Logger;

@ApplicationScoped
public class KafkaMessageService {

    private static final Logger LOGGER = Logger.getLogger(KafkaMessageService.class.getName());

    @Inject
    @Channel("payments")
    Emitter<String> paymentsEmitter;

    @Inject
    @ChargedSubscriber
    Event<SubscriberChargedPayload> subscriberChargedEvent;

    @Transactional
    @Incoming("post-payments")
    public void consumePostPaymentMessage(String message) {
        PaymentConfirmation paymentConfirmation = JsonbBuilder.create().fromJson(message, PaymentConfirmation.class);
        Subscriber subscriber = Subscriber.find("userName", paymentConfirmation.username()).firstResult();
        LOGGER.info("Received payment confirmation for username %s and status %s".formatted(paymentConfirmation.username(), paymentConfirmation.confirmationDTO().getSuccess()));

        if (subscriber == null) {
            LOGGER.warning("No subscriber with the user name of '%s' was found.".formatted(paymentConfirmation.username()));
        }

        SubscriberChargedPayload eventPayload = new SubscriberChargedPayload(subscriber, paymentConfirmation.confirmationDTO());
        subscriberChargedEvent.fire(eventPayload);
    }

    public void sendPaymentsMessage(PaymentPayload payload) {
        String payloadString = JsonbBuilder.create().toJson(payload);
        paymentsEmitter.send(payloadString).toCompletableFuture().join();
        LOGGER.info("Successfully emitted message to payments topic: %s".formatted(payloadString));
    }

}
