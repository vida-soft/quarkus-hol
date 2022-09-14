package com.vidasoft.magman.article;

import com.vidasoft.magman.model.Article;

public class ArticleDTO {

    private Long id;
    private String title;
    private String content;
    private String publishDate;
    private Long authorId;

    public ArticleDTO() {
    }

    public ArticleDTO(Article article) {
        this.id = article.id;
        this.title = article.title;
        this.content = article.content;
        this.publishDate = article.publishDate.toString();
        this.authorId = article.author.id;
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
}
