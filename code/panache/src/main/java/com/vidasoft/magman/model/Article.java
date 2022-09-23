package com.vidasoft.magman.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@NamedQuery(name = Article.GET_ALL_ARTICLES, query = "select a from Article a")
@NamedQuery(name = Article.GET_ALL_ARTICLES_FROM_AUTHOR, query = "select a from Article a where a.author.id = :authorId")
@NamedQuery(name = Article.DELETE_ARTICLE, query = "delete from Article a where a.id = :articleId")
public class Article extends AbstractEntity {

    public static final String GET_ALL_ARTICLES = "Article.getAllArticles";
    public static final String GET_ALL_ARTICLES_FROM_AUTHOR = "Article.getAllArticlesFromAuthor";
    public static final String DELETE_ARTICLE = "Article.deleteArticle";

    public String title;

    @Column(length = 10_000)
    public String content;
    public LocalDate publishDate;

    @ManyToOne
    public Author author;

    @OneToMany
    public List<Comment> comments = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "article_advertiser",
            joinColumns = @JoinColumn(name = "advertiser_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    public Set<Advertiser> advertisers;

    public Article() {
    }

    public Article(String title, String content, LocalDate publishDate, Author author) {
        this.title = title;
        this.content = content;
        this.publishDate = publishDate;
        this.author = author;
    }
}
