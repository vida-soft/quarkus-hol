package com.vidasoft.magman.sse;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
public class SseResource implements SseAPI {

    @Inject
    EventBus bus;

    String userId;

    public SseResource(JsonWebToken jwt) {
        userId = jwt.getClaim("upn");
    }

    @Override
    public Multi<String> stream() {
        return bus.<String>consumer(userId)
                .bodyStream().toMulti();
    }
}
