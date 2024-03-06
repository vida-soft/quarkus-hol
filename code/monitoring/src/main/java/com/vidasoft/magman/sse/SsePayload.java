package com.vidasoft.magman.sse;

import jakarta.json.bind.JsonbBuilder;

public record SsePayload(Type type, String message) {

    @Override
    public String toString() {
        return JsonbBuilder.create().toJson(this);
    }

    public enum Type {
        PAYMENTS, POST_PAYMENTS
    }
}
