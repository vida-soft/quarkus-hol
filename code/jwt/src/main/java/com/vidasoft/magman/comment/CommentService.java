package com.vidasoft.magman.comment;

import com.vidasoft.magman.interceptors.CreatesContent;
import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Comment;
import com.vidasoft.magman.model.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class CommentService {

    @Inject
    EntityManager entityManager;

    @CreatesContent
    public Comment createComment(String content, User author, Article article) {
        Comment comment = new Comment(content, author);
        comment.article = article;
        comment.persist();
        return comment;
    }

    public Optional<CommentDTO> getCommentById(long commentId) {
        return entityManager.createNamedQuery(Comment.GET_COMMENT_BY_ID, CommentDTO.class)
                .setParameter("commentId", commentId)
                .getResultStream()
                .findFirst();
    }

}
