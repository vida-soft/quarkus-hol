package com.vidasoft.magman.article;

import com.vidasoft.magman.model.Advertiser;
import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Author;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Path("/article")
public class ArticleResource {

    @Inject
    ArticleServiceImpl articleService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createArticle(ArticleDTO articleDTO) {
        Author author = Author.findById(articleDTO.getAuthorId());
        if (author == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            Article article = articleService.createArticle(articleDTO.getTitle(), articleDTO.getContent(), author);
            return Response.created(URI.create(String.format("article/%s", article.id))).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticle(@PathParam("id") Long articleId) {
        if (articleId < 1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return articleService.getArticle(articleId)
                .map(article -> Response.ok(new ArticleDTO(article)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response editArticle(@PathParam("id") Long articleId, ArticleDTO articleDTO) {
        if (articleId < 1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Article article = Article.findById(articleId);

        if (article == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            articleService.editArticle(article, articleDTO.getTitle(), articleDTO.getContent());
            return Response.ok(new ArticleDTO(article)).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void deleteArticle(@PathParam("id") Long articleId) {
        Article.delete("id", articleId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticles(@QueryParam("page") @DefaultValue("1") int page,
                                @QueryParam("size") @DefaultValue("10") int size,
                                @QueryParam("author") Long authorId) {
        if (page < 1 || size < 0 || authorId != null && authorId < 1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            List<Article> articles = (authorId == null ? Article.findAll() :
                    Article.find("author.id = ?1", authorId))
                    .page(page - 1, size)
                    .list();

            return Response.ok(articles.stream().map(ArticleDTO::new).collect(Collectors.toList())).build();
        }
    }

    @PATCH
    @Transactional
    @Path("{id}/advertiser/{advertiserId}")
    public Response addAdvertiserToArticle(@PathParam("id") Long id, @PathParam("advertiserId") Long advertiserId) {
        if (id < 1 || advertiserId < 1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Article article = Article.findById(id);
        Advertiser advertiser = Advertiser.findById(advertiserId);

        if (article == null || advertiser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        article.advertisers.add(advertiser);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
