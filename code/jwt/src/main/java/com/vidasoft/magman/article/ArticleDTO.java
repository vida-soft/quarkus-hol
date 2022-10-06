package com.vidasoft.magman.article;

import com.vidasoft.magman.comment.CommentDTO;
import com.vidasoft.magman.model.Article;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ArticleDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 225, message = "The title of the article must be between {min} and {max} characters")
    private String title;

    @NotBlank
    @Size(min = 1, max = 10_000)
    private String content;
    private String publishDate;

    private String lastModified;
    private Long authorId;

    private List<CommentDTO> comments;

    public ArticleDTO() {
    }

    public ArticleDTO(Article article) {
        this.id = article.id;
        this.title = article.title;
        this.content = article.content;
        this.publishDate = Optional.ofNullable(article.publishDate).map(Objects::toString).orElse(null);
        this.authorId = article.author.id;
        this.lastModified = Optional.ofNullable(article.lastModified).map(Objects::toString).orElse(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
}
