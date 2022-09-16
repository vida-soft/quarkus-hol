package com.vidasoft.magman.comment;

import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Comment;
import com.vidasoft.magman.model.User;

import javax.transaction.Transactional;
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

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createComment(@PathParam("id") Long articleId, CommentDTO commentDTO) {
        if (articleId == null || articleId < 1 || commentDTO.getAuthorId() == null || commentDTO.getAuthorId() < 1) {
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
        if (commentId == null || commentId < 1) {
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
