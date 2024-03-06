package com.vidasoft.magman.user;

import com.vidasoft.magman.model.Author;
import com.vidasoft.magman.model.Manager;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Authenticated
@Path("/user/author")
@Tag(name = "Author resource", description = "Contains endpoints regarding authors")
public interface AuthorAPI {
    @GET
    @Path("/{id}")
    @RolesAllowed({Manager.ROLE_NAME, Author.ROLE_NAME})
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getAuthor",
            summary = "Get author",
            description = "Gets detailed information about the author"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "The found author",
                    content = @Content(
                            schema = @Schema(implementation = AuthorDTO.class)

                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "The provided author id is not valid"
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "There is no authorization token provided or it has expired"
            ),
            @APIResponse(
              responseCode = "403",
              description = "This user does not have access to the author information."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "An author with this id could not be found"
            )
    })
    Response getAuthor(@Schema(example = "123") @PathParam("id") Long authorId);
}
