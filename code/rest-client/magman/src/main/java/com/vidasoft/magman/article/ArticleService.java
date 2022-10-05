package com.vidasoft.magman.article;

import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Author;

import java.util.Optional;

public interface ArticleService {

    Optional<Article> getArticle(long articleId);

    Article createArticle(String title, String content, Author author);

    void editArticle(Article article, String title, String content);

}
