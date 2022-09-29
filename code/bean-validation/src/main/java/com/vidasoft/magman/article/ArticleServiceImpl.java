package com.vidasoft.magman.article;

import com.vidasoft.magman.interceptors.CreatesContent;
import com.vidasoft.magman.interceptors.ModifiesContent;
import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Author;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class ArticleServiceImpl implements ArticleService {

    public Optional<Article> getArticle(long articleId) {
        return Article.findByIdOptional(articleId);
    }

    @CreatesContent
    public Article createArticle(String title, String content, Author author) {
        Article article = new Article(title, content, author);
        article.persist();
        return article;
    }

    @ModifiesContent
    public void editArticle(Article article, String title, String content) {
        article.title = title;
        article.content = content;
    }

}
