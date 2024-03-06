package com.vidasoft.magman.messaging;

import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.spendpal.ConfirmationDTO;
import com.vidasoft.magman.spendpal.SpendPalClient;
import com.vidasoft.magman.spendpal.SpendPalException;
import com.vidasoft.magman.sse.SsePayload;
import com.vidasoft.magman.subscription.ChargedSubscriber;
import com.vidasoft.magman.subscription.CreditCardDTO;
import com.vidasoft.magman.subscription.SubscriberChargedPayload;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class KafkaMessageService {
    private static final Logger LOGGER = Logger.getLogger(KafkaMessageService.class.getName());

    private Set<PaymentPayload> pendingMessages = ConcurrentHashMap.newKeySet(); //We are adding entries from different threads, so we need a set that could support it

    @Inject
    @Channel("payments")
    Emitter<String> paymentsEmitter;

    @Inject
    @ChargedSubscriber
    Event<SubscriberChargedPayload> subscriberChargedEvent;

    @Inject
    EventBus eventBus;

    @Inject
    @RestClient
    SpendPalClient spendPalClient;

    @Inject
    MeterRegistry registry;

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
    @Timeout(5000)
    @Fallback(fallbackMethod = "chargeSubscriberThroughRest")
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Asynchronous
    public CompletionStage<Void> sendPaymentsMessage(PaymentPayload payload) {
        pendingMessages.add(payload);
        registry.gaugeCollectionSize("payments_to_send", List.of(Tag.of("Tag1", "Tag1")), pendingMessages);
        LOGGER.info("Attempting to send payment message");
        String payloadString = JsonbBuilder.create().toJson(payload);
        paymentsEmitter.send(payloadString).toCompletableFuture().join();
        LOGGER.info("Successfully emitted message to payments topic: %s".formatted(payloadString));

        registry.gaugeCollectionSize("payments_to_send", List.of(Tag.of("Tag1", "Tag1")), pendingMessages);
        pendingMessages.remove(payload);
        return CompletableFuture.completedFuture(null);
    }

    @Transactional
    public CompletionStage<Void> chargeSubscriberThroughRest(PaymentPayload payload) throws SpendPalException {
        Subscriber subscriber = Subscriber.find("userName", payload.username()).firstResult();
        ConfirmationDTO paymentResult = spendPalClient.chargeCustomer(new CreditCardDTO(subscriber.creditCard));
        LOGGER.log(Level.INFO, "Charging subscriber with id: {0}  and card type {1} of number: {2}",
                new Object[]{subscriber.id, subscriber.creditCard.creditCardType, subscriber.creditCard.number});

        if (paymentResult.getSuccess()) {
            LOGGER.log(Level.INFO, "Successfully charged customer with id: {0}  and card type {1} of number: {2}",
                    new Object[]{subscriber.id, subscriber.creditCard.creditCardType, subscriber.creditCard.number});
            subscriberChargedEvent.fire(new SubscriberChargedPayload(subscriber, paymentResult));
        } else {
            LOGGER.log(Level.WARNING, "Unable to charge customer with id: {0}  and card type {1} of number: {2}",
                    new Object[]{subscriber.id, subscriber.creditCard.creditCardType, subscriber.creditCard.number});
            // Will probably email the customer, or, most likely, call the police.
            // Failed payments feel like federal crime after all ¯\_(ツ)_/¯
        }

        return CompletableFuture.completedFuture(null);
    }

}
