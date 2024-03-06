package com.vidasoft.magman.comment;

import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Comment;
import com.vidasoft.magman.model.User;
import com.vidasoft.magman.security.LoggedUser;
import com.vidasoft.magman.validator.ValidationService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.sql.Time;
import java.util.List;

public class CommentResource implements CommentAPI {

    @Inject
    CommentService commentService;

    @Inject
    ValidationService validationService;

    @Inject
    @LoggedUser
    User loggedUser;

    @Inject
    MeterRegistry registry;

    private Timer createCommentTimer;
    private Timer getCommentsTimer;

    @PostConstruct
    void init() {
        createCommentTimer = registry.timer("comments_create_comment");
        getCommentsTimer = Timer.builder("comments_get_timer")
                .tag("comments_resource", "get_comments")
                .register(registry);
    }

    @Override
    @Transactional
    public Response createComment(Long articleId, CommentDTO commentDTO) {
        Timer.Sample sample = Timer.start();

        var commentViolations = validationService.validateObject(commentDTO);
        if (commentViolations.size() > 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity(commentViolations).build();
        }

        Article article = Article.findById(articleId);
        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Comment comment = commentService.createComment(commentDTO.getContent(), loggedUser, article);

        sample.stop(createCommentTimer);

        return Response.created(URI.create(String.format("/article/%d/comment/%d", articleId, comment.id))).build();
    }

    @Override
    public Response getCommentById(Long commentId) {
        return commentService.getCommentById(commentId)
                .map(c -> Response.ok(c).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @Override
    public List<CommentDTO> getCommentsForArticle(Long articleId) {
        return getCommentsTimer.record(() -> Comment.findByArticleId(articleId)
                .stream().map(CommentDTO::new).toList());
    }
}
