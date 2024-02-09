package com.vidasoft.spendpal;

import com.vidasoft.spendpal.dto.ConfirmationDTO;
import com.vidasoft.spendpal.dto.PaymentConfirmation;
import com.vidasoft.spendpal.dto.PaymentPayload;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.JsonbBuilder;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class KafkaMessageService {
    private static final Logger LOGGER = Logger.getLogger(KafkaMessageService.class.getName());

    @Inject
    @Channel("post-payments")
    Emitter<String> postPaymentsEmitter;

    @Incoming("payments")
    public void consumePostPaymentMessage(String message) {
        PaymentPayload payload = JsonbBuilder.create().fromJson(message, PaymentPayload.class);
        LOGGER.info("Received message with payload: %s".formatted(message));

        PaymentConfirmation paymentConfirmation = new PaymentConfirmation(payload.username(), new ConfirmationDTO(true, LocalDateTime.now()));
        sendPaymentsMessage(paymentConfirmation);
    }

    public void sendPaymentsMessage(PaymentConfirmation confirmation) {
        String payload = JsonbBuilder.create().toJson(confirmation);
        postPaymentsEmitter.send(payload);
        LOGGER.info("Successfully sent payment confirmations with payload: %s".formatted(payload));
    }
}
