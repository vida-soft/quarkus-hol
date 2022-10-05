package com.vidasoft.magman.model;

import com.vidasoft.magman.comment.CommentDTO;

import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SqlResultSetMapping;
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
public class Comment extends PublishedContent {

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
}
