package com.vidasoft.magman.sse;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.core.eventbus.EventBus;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.SseElementType;

@RequestScoped
@Authenticated
@Path("sse")
public class SseResource {

    @Inject
    EventBus bus;

    String userId;

    public SseResource(JsonWebToken jwt) {
        userId = jwt.getClaim("upn");
    }

    @POST
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.TEXT_PLAIN)
    public Multi<String> stream() {
        return bus.<String>consumer(userId)
                .bodyStream().toMulti();
    }
}
