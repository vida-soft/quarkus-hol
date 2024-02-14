package com.vidasoft.magman.messaging;

import com.vidasoft.magman.spendpal.ConfirmationDTO;

public record PaymentConfirmation(String username, ConfirmationDTO confirmationDTO) {
}
