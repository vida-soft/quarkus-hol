package com.vidasoft.magman.article;

import com.vidasoft.magman.model.Advertiser;
import com.vidasoft.magman.model.Article;
import com.vidasoft.magman.model.Author;
import org.jboss.logging.annotations.Pos;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
    public Response createArticle(@Valid @NotNull ArticleDTO articleDTO) {
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
    public Response getArticle(@Positive @PathParam("id") Long articleId) {
        return articleService.getArticle(articleId)
                .map(article -> Response.ok(new ArticleDTO(article)).build())
                .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response editArticle(@Positive @PathParam("id") Long articleId, @Valid @NotNull ArticleDTO articleDTO) {
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
    public void deleteArticle(@Positive @PathParam("id") Long articleId) {
        Article.delete("id", articleId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticles(@QueryParam("page") @DefaultValue("1") @Positive int page,
                                @QueryParam("size") @DefaultValue("10") @Positive int size,
                                @QueryParam("author") @Positive Long authorId) {

        List<Article> articles = (authorId == null ? Article.findAll() :
                Article.find("author.id = ?1", authorId))
                .page(page - 1, size)
                .list();

        return Response.ok(articles.stream().map(ArticleDTO::new).collect(Collectors.toList())).build();
    }

    @PATCH
    @Transactional
    @Path("{id}/advertiser/{advertiserId}")
    public Response addAdvertiserToArticle(@Positive @PathParam("id") Long id, @Positive @PathParam("advertiserId") Long advertiserId) {
        Article article = Article.findById(id);
        Advertiser advertiser = Advertiser.findById(advertiserId);

        if (article == null || advertiser == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        article.advertisers.add(advertiser);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
