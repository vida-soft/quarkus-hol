package com.vidasoft.magman.model;

import io.quarkus.panache.common.Sort;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
public class Subscription extends AbstractEntity {

    @ManyToOne
    public Subscriber subscriber;

    @Enumerated(EnumType.STRING)
    public SubscriptionStatus status = SubscriptionStatus.PENDING;

    public LocalDateTime initiated = LocalDateTime.now();

    public LocalDateTime completed;

    public Subscription() {

    }

    public Subscription(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    public static Optional<Subscription> findLastPendingSubscription(Subscriber subscriber) {
        return find("subscriber=?1 and status='PENDING'", Sort.descending("initiated"), subscriber)
                .firstResultOptional();
    }

    public static long countPendingSubscriptions() {
        return Subscription.count("status", SubscriptionStatus.PENDING);
    }
}
