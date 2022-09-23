package com.vidasoft.magman.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import java.util.Set;

@Entity
@NamedQuery(name = Article.GET_ARTICLE_WITH_COMMENTS,
        query = "select new com.vidasoft.magman.model.ArticleWithComment(a, c) from Article a left join Comment c on c.article = a where a.id = :articleId")
public class Article extends PublishedContent {

    public static final String GET_ARTICLE_WITH_COMMENTS = "Article.getArticleWithComments";
    public String title;

    @Column(length = 10_000)
    public String content;

    @ManyToOne
    public Author author;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "article_advertiser",
            joinColumns = @JoinColumn(name = "advertiser_id"),
            inverseJoinColumns = @JoinColumn(name = "article_id")
    )
    public Set<Advertiser> advertisers;

    public Article() {
    }

    public Article(String title, String content, Author author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
