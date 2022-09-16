package com.vidasoft.magman.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import java.time.LocalDate;

@Entity
@NamedQuery(name = Article.GET_ARTICLE_WITH_COMMENTS,
        query = "select new com.vidasoft.magman.model.ArticleWithComment(a, c) from Article a left join Comment c on c.article = a where a.id = :articleId")
public class Article extends AbstractEntity {

    public static final String GET_ARTICLE_WITH_COMMENTS = "Article.getArticleWithComments";
    public String title;

    @Column(length = 10_000)
    public String content;
    public LocalDate publishDate;

    @ManyToOne
    public Author author;

    public Article() {
    }

    public Article(String title, String content, LocalDate publishDate, Author author) {
        this.title = title;
        this.content = content;
        this.publishDate = publishDate;
        this.author = author;
    }
}
