package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.spendpal.ConfirmationDTO;

public record SubscriberChargedPayload(Subscriber subscriber, ConfirmationDTO confirmation) {
}
