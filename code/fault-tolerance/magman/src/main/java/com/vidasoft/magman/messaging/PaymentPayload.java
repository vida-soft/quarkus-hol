package com.vidasoft.magman.messaging;

import com.vidasoft.magman.subscription.CreditCardDTO;

public record PaymentPayload(String username, CreditCardDTO creditCardDTO) {
}
