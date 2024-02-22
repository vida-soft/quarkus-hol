package com.vidasoft.magman.sse;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Multi;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.annotations.SseElementType;

@Authenticated
@Path("sse")
@Tag(name = "SSE Resource", description = "Allows for the users to receive notifications, regarding their accounts")
public interface SseAPI {
    @POST
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType(MediaType.TEXT_PLAIN)
    @Operation(
            operationId = "stream",
            summary = "Start listening for SSE events",
            description = "Subscribes to an SSE stream and starts receiving events once they arrive."
    )
    @APIResponse(
            responseCode = "200",
            description = "Active SSE stream to listen for new notifications",
            content = @Content(
                    schema = @Schema(implementation = Multi.class)
            )
    )
    Multi<String> stream();
}
