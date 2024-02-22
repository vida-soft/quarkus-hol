package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.Subscriber;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Authenticated
@Path("subscription")
@Tag(name = "Subscription Resource", description = "Contains all the endpoints regarding subscriptions")
public interface SubscriptionAPI {
    @PUT
    @Transactional
    @Path("{userId}")
    @RolesAllowed({Subscriber.ROLE_NAME})
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "addPaymentMethod",
            summary = "Add payment method",
            description = "Adda a payment instrument to the customer"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "The credit card was added to the user's account"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "The provided card details are invalid. Please read the error message"
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "There is no authorization header provided or it has expired"
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "The user type is not allowed to add payment methods to their account"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Could not find subscriber with that id"
            )
    })
    Response addPaymentMethod(@Schema(example = "123") @Positive @PathParam("userId") Long userId, @Valid @NotNull CreditCardDTO creditCardDTO);

    @POST
    @RolesAllowed({Subscriber.ROLE_NAME})
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "chargeSubscriber",
            summary = "Charge subscriber",
            description = "Charge subscriber's credit card to extend subscription"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "The request to charge the subscriber has been sent to SpendPal"
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "There is no authorization header provided or it has expired"
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "The logged user cannot perform this operation"
            ),
            @APIResponse(
                    responseCode = "406",
                    description = "The payment service cannot accept the payment instrument or the logged user is not of type Subscriber"
            )
    })
    Response chargeSubscriber();
}
