package com.vidasoft.magman.comment;

import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Comment;
import com.vidasoft.magman.model.User;
import com.vidasoft.magman.security.LoggedUser;
import com.vidasoft.magman.validator.ValidationService;
import io.quarkus.security.Authenticated;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
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
import java.math.BigInteger;
import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Authenticated
@Path("/article/{id}/comment")
public class CommentResource {

    @Inject
    CommentService commentService;

    @Inject
    ValidationService validationService;

    @Inject
    @LoggedUser
    User loggedUser;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createComment(@NotNull @Positive @PathParam("id") Long articleId, CommentDTO commentDTO) {
        var commentViolations = validationService.validateObject(commentDTO);
        if (commentViolations.size() > 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity(commentViolations).build();
        }

        Article article = Article.findById(articleId);
        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Comment comment = commentService.createComment(commentDTO.getContent(), loggedUser, article);

        return Response.created(URI.create(String.format("/article/%d/comment/%d", articleId, comment.id))).build();
    }

    @GET
    @Path("/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommentById(@Positive @PathParam("commentId") Long commentId) {
        return commentService.getCommentById(commentId)
                .map(c -> Response.ok(c).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
