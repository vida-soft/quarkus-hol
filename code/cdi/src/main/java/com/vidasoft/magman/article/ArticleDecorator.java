package com.vidasoft.magman.article;

import com.vidasoft.magman.model.Article;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Collectors;

@Decorator
public abstract class ArticleDecorator implements ArticleService {

    @Inject
    @Delegate
    ArticleService articleService;

    @Override
    public Optional<Article> getArticle(long articleId) {
        return articleService.getArticle(articleId)
                .map(this::decorateArticle);
    }

    private Article decorateArticle(Article article) {
        var advertisers = article.advertisers;
        var message = String.format("\nThis article has been sponsored by: %s",
                advertisers.stream().map(a -> a.name).collect(Collectors.joining(", ")));
        var decoratedArticle = new Article(article.title, article.content + message, article.author);
        decoratedArticle.id = article.id;
        return decoratedArticle;
    }
}
