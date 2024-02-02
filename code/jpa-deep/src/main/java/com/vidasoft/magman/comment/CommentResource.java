package com.vidasoft.magman.comment;

import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Comment;
import com.vidasoft.magman.model.User;

import jakarta.transaction.Transactional;
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

@Path("/article/{id}/comment")
public class CommentResource {

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createComment(@PathParam("id") Long articleId, CommentDTO commentDTO) {
        if (articleId < 1 || commentDTO.getAuthorId() == null || commentDTO.getAuthorId() < 1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        User author = User.findById(commentDTO.getAuthorId());
        if (author == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Article article = Article.findById(articleId);
        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Comment comment = new Comment(commentDTO.getContent(), author, LocalDateTime.now());
        comment.article = article;
        comment.persist();

        return Response.created(URI.create(String.format("/article/%d/comment/%d", articleId, comment.id))).build();
    }

    @GET
    @Path("/{commentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCommentById(@PathParam("commentId") Long commentId) {
        if (commentId < 1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            CommentDTO comment = Comment.getEntityManager().createNamedQuery(Comment.GET_COMMENT_BY_ID, CommentDTO.class)
                    .setParameter("commentId", commentId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
            if (comment == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok(comment).build();
            }
        }
    }
}
