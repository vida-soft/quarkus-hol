package com.vidasoft.magman.messaging;

import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.sse.SsePayload;
import com.vidasoft.magman.subscription.ChargedSubscriber;
import com.vidasoft.magman.subscription.SubscriberChargedPayload;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
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

    @Inject
    EventBus eventBus;

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
        eventBus.send(subscriber.id + "", new SsePayload(SsePayload.Type.POST_PAYMENTS, message).toString());
        subscriberChargedEvent.fire(eventPayload);
    }

    @Retry
    @Asynchronous
    public Future<Void> sendPaymentsMessage(Long userId, PaymentPayload payload) {
        LOGGER.info("Attempting to send payment message");
        String payloadString = JsonbBuilder.create().toJson(payload);
        paymentsEmitter.send(payloadString)
                .thenRun(() ->
                        eventBus.send(userId.toString(), new SsePayload(SsePayload.Type.PAYMENTS, "Payment information sent!").toString()))
                .toCompletableFuture().join();
        LOGGER.info("Successfully emitted message to payments topic: %s".formatted(payloadString));

        return CompletableFuture.completedFuture(null);
    }

}
