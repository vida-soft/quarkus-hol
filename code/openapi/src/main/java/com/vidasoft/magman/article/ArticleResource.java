package com.vidasoft.magman.article;

import com.vidasoft.magman.model.Advertiser;
import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Author;
import com.vidasoft.magman.model.User;
import com.vidasoft.magman.security.LoggedUser;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class ArticleResource implements ArticleAPI {

    @Inject
    ArticleService articleService;

    @Inject
    @LoggedUser
    User loggedUser;


    @Override
    @Transactional
    public Response createArticle(ArticleDTO articleDTO) {
        if (loggedUser instanceof Author author) {
            Article article = articleService.createArticle(articleDTO.getTitle(), articleDTO.getContent(), author);
            return Response.created(URI.create(String.format("article/%s", article.id))).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }


    @Override
    public Response getArticle(Long articleId) {
        return articleService.getArticle(articleId)
                .map(article -> Response.ok(new ArticleDTO(article)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }


    @Override
    @Transactional
    public Response editArticle(Long articleId, ArticleDTO articleDTO) {
        Article article = Article.findById(articleId);

        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else if (!loggedUser.equals(article.author)) {                //assuming AbstractEntity.equals is overridden
            return Response.status(Response.Status.FORBIDDEN).build();
        } else {
            articleService.editArticle(article, articleDTO.getTitle(), articleDTO.getContent());
            return Response.ok(new ArticleDTO(article)).build();
        }
    }


    @Override
    @Transactional
    public void deleteArticle(Long articleId) {
        Article.delete(articleId, loggedUser.id);
    }


    @Override
    public Response getArticles(int page, int size, Long authorId) {

        List<Article> articles = (authorId == null ? Article.findAll() :
                Article.find("author.id = ?1", authorId))
                .page(page - 1, size)
                .list();

        return Response.ok(articles.stream().map(ArticleDTO::new).collect(Collectors.toList())).build();
    }


    @Override
    @Transactional
    public Response addAdvertiserToArticle(Long id, Long advertiserId) {
        Article article = Article.findById(id);
        Advertiser advertiser = Advertiser.findById(advertiserId);

        if (article == null || advertiser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        article.advertisers.add(advertiser);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
