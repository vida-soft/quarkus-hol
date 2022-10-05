package com.vidasoft.magman.comment;

import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Comment;
import com.vidasoft.magman.model.User;
import com.vidasoft.magman.validator.ValidationService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigInteger;
import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Path("/article/{id}/comment")
public class CommentResource {

    @Inject
    CommentService commentService;

    @Inject
    ValidationService validationService;

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createComment(@NotNull @Positive @PathParam("id") Long articleId, CommentDTO commentDTO) {
        var commentViolations = validationService.validateObject(commentDTO);
        if (commentViolations.size() > 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity(commentViolations).build();
        }

        User author = User.findById(commentDTO.getAuthorId());
        if (author == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Article article = Article.findById(articleId);
        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Comment comment = commentService.createComment(commentDTO.getContent(), author, article);

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
