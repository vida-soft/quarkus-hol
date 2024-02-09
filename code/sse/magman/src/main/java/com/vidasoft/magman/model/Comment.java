package com.vidasoft.magman.model;

import com.vidasoft.magman.comment.CommentDTO;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SqlResultSetMapping;
import java.time.LocalDateTime;

@Entity
@SqlResultSetMapping(name = Comment.GET_COMMENT_BY_ID,
        classes = {
                @ConstructorResult(targetClass = CommentDTO.class, columns = {
                        @ColumnResult(name = "ID", type = Long.class),
                        @ColumnResult(name = "CONTENT"),
                        @ColumnResult(name = "AUTHOR_ID", type = Long.class),
                        @ColumnResult(name = "CREATED", type = String.class)
                })
        })
@NamedNativeQuery(name = Comment.GET_COMMENT_BY_ID,
        query = "select ID, CONTENT, AUTHOR_ID, CREATED from COMMENT where id = :commentId",
        resultSetMapping = Comment.GET_COMMENT_BY_ID)
@NamedQuery(name = Comment.DELETE_ALL_WHERE_AUTHOR_OR_MANAGER, query = "delete from Comment c where c.article.id = :articleId " +
        "and (c.article.author.id = :userId or (select count (m) from Manager m where m.id = :userId) > 0)")
public class Comment extends PublishedContent {

    public static final String DELETE_ALL_WHERE_AUTHOR_OR_MANAGER = "Comment.deleteAllWhereAuthorOrManager";

    public static final String GET_COMMENT_BY_ID = "getCommentById";

    public String content;

    @ManyToOne
    public User author;

    @ManyToOne
    public Article article;

    public Comment() {
    }

    public Comment(String content, User author) {
        this.content = content;
        this.author = author;
    }

    public static void deleteAllForArticle(long articleId, long userId) {
        getEntityManager().createNamedQuery(DELETE_ALL_WHERE_AUTHOR_OR_MANAGER)
                .setParameter("articleId", articleId)
                .setParameter("userId", userId);
    }
}
