package com.vidasoft.magman.comment;

import com.vidasoft.magman.model.Comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.Optional;

public class CommentDTO {

    private Long id;

    @NotBlank
    @Size(max = 255)
    private String content;

    @NotNull
    @Positive
    private Long authorId;
    private String created;

    public CommentDTO() {
    }

    public CommentDTO(Long id, String content, Long authorId, String created) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.created = created;
    }

    public CommentDTO(Comment comment) {
        id = comment.id;
        content = comment.content;
        authorId = comment.author.id;
        created = Optional.ofNullable(comment.publishDate).map(Objects::toString).orElse(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
