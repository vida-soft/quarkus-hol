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
@NamedQuery(name = Article.DELETE_ARTICLE_IF_ALLOWED,
        query = "delete from Article a where a.id = :articleId and (a.author.id = :userId or (select count (m) from Manager m where m.id = :userId) > 0)")
public class Article extends PublishedContent {

    public static final String DELETE_ARTICLE_IF_ALLOWED = "Article.deleteArticleIfAllowed";

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

    public static void delete(long articleId, long userId) {
        Comment.deleteAllForArticle(articleId, userId);
        getEntityManager().createNamedQuery(DELETE_ARTICLE_IF_ALLOWED)
                .setParameter("articleId", articleId)
                .setParameter("userId", userId)
                .executeUpdate();
    }
}
