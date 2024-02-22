package com.vidasoft.magman.article;

import com.vidasoft.magman.comment.CommentDTO;
import com.vidasoft.magman.model.Article;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Schema(description = "Contains data about the article")
public class ArticleDTO {

    @Schema(description = "The id of the article", example = "1234")
    private Long id;

    @NotBlank
    @Schema(description = "The title of the article", example = "The quick brown fox jumps over the lazy dog!")
    @Size(min = 1, max = 225, message = "The title of the article must be between {min} and {max} characters")
    private String title;

    @NotBlank
    @Schema(description = "The article's content.", example = "This is a long article about a quick fox that is brown and jumps over a lazy dog that is lazy")
    @Size(min = 1, max = 10_000)
    private String content;

    @Schema(description = "The date when the article was published on.", example = "2022-09-13T00:00")
    private String publishDate;

    @Schema(description = "The date this article was last modified. Might differ from the publish date.", example = "2022-09-13T00:01")
    private String lastModified;

    @Schema(description = "The id of the author this article is written by.", example = "256")
    private Long authorId;

    @Schema(description = "Comments by article readers")
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
