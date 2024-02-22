package com.vidasoft.magman.comment;

import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Comment;
import com.vidasoft.magman.model.User;
import com.vidasoft.magman.security.LoggedUser;
import com.vidasoft.magman.validator.ValidationService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.net.URI;

public class CommentResource implements CommentAPI {

    @Inject
    CommentService commentService;

    @Inject
    ValidationService validationService;

    @Inject
    @LoggedUser
    User loggedUser;

    @Override
    @Transactional
    public Response createComment(Long articleId, CommentDTO commentDTO) {
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

    @Override
    public Response getCommentById(Long articleId, Long commentId) {
        return commentService.getCommentById(commentId)
                .map(c -> Response.ok(c).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }
}
