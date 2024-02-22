package com.vidasoft.magman.advertiser;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Path("advertiser")
@Tag(name = "Advertiser Resource", description = "Used to manage advertisers")
public interface AdvertiserAPI {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getAllAdvertisers",
            summary = "Get all advertisers",
            description = "Returns a list of all advertisers"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "A list of all advertisers",
                    content = @Content(
                            schema = @Schema(implementation = AdvertiserDTO[].class)
                    )
            )
    })
    List<AdvertiserDTO> getAllAdvertisers();
}
