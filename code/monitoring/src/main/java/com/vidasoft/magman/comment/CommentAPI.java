package com.vidasoft.magman.comment;

import io.quarkus.security.Authenticated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@Authenticated
@Path("/article/{id}/comment")
@Tag(name = "Comments Resource", description = "Contains all the CRUD operations for comments")
public interface CommentAPI {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "createComment",
            description = "Allows you to create a new comment and attack it to an article",
            summary = "Create a comment to an article"
    )
    @RequestBody(
            name = "The comment entry",
            content = @Content(schema =
            @Schema(
                    implementation = CommentDTO.class,
                    example = """
                            {
                              "content": "I loved this article. It is true that dogs are lazy when foxes are quick."
                            }
                            """
            ))
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    description = "The comment was successfully created",
                    headers = @Header(
                            name = "location",
                            description = "The location of the comment",
                            schema = @Schema(
                                    implementation = String.class,
                                    example = "http://localhost:8080/article/1/comment/3"
                            )
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "The provided DTO or article ID are invalid"
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "You need to update your Authorization token"
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "There was no article with the given id to attach comment to"
            )
    })
    Response createComment(@Parameter(example = "123") @NotNull @Positive @PathParam("id") Long articleId, CommentDTO commentDTO);

    @GET
    @Path("/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getCommentById",
            summary = "Find a comment within an article by its id"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = CommentDTO.class)
                    )
            )
    })
    Response getCommentById(@Parameter(example = "235") @Positive @PathParam("commentId") Long commentId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getCommentsForArticle",
            summary = "Returns all the comments for an article by its id"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    content = @Content(
                            schema = @Schema(implementation = CommentDTO[].class)
                    )
            )
    })
    List<CommentDTO> getCommentsForArticle(@Parameter(example = "123") @NotNull @Positive @PathParam("id") Long articleId);
}
