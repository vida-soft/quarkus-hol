package com.vidasoft.magman.subscription;

import com.vidasoft.magman.messaging.KafkaMessageService;
import com.vidasoft.magman.messaging.PaymentPayload;
import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.model.Subscription;
import com.vidasoft.magman.model.SubscriptionStatus;
import com.vidasoft.magman.sse.SsePayload;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class PaymentService {

    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());

    @Inject
    EventBus eventBus;

    @Inject
    KafkaMessageService kafkaMessageService;

    @Inject
    MeterRegistry registry;

    public void buildPendingTransactionsGauge(@Observes StartupEvent startupEvent) {
        Gauge.builder("subscriptions_pending", Subscription::countPendingSubscriptions).register(registry);
    }


    @Timed(value = "charge_subscriber_execution_time", description = "Tracks how much time it takes to charge a subscriber")
    public boolean chargeSubscriber(Subscriber subscriber) {
        Subscription subscription = createSubscription(subscriber);

        if (subscriber.creditCard != null) {
            CreditCardDTO creditCardDTO = new CreditCardDTO(subscriber.creditCard);
            kafkaMessageService.sendPaymentsMessage(new PaymentPayload(subscriber.userName, creditCardDTO))
                    .thenRun(() -> eventBus.send(subscriber.id + "",
                            new SsePayload(SsePayload.Type.PAYMENTS, "Payment information sent!").toString()))
                    .exceptionally(throwable -> {
                        LOGGER.severe(throwable.getMessage());
                        eventBus.send(subscriber.id + "", new SsePayload(SsePayload.Type.PAYMENTS, "Error making subscription. " +
                                "Please retry making a subscription: %s".formatted(throwable.getMessage())));
                        return null;
                    });
            return true;
        } else {
            subscription.status = SubscriptionStatus.FAILED;
            subscription.completed = LocalDateTime.now();
            return false;
        }
    }

    @Transactional
    public Subscription createSubscription(Subscriber subscriber) {
        failPreviousSubscriptionAttempt(subscriber);
        Subscription subscription = new Subscription(subscriber);
        subscription.persist();
        return subscription;
    }

    private void failPreviousSubscriptionAttempt(Subscriber subscriber) {
        Subscription.findLastPendingSubscription(subscriber)
                .ifPresent(s -> {
                    s.status = SubscriptionStatus.FAILED;
                    s.completed = LocalDateTime.now();
                });
    }
}
