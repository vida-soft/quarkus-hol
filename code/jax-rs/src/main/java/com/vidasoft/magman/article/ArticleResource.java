package com.vidasoft.magman.article;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Path("/article")
public class ArticleResource {

    private static Map<Long, ArticleDTO> articles = new HashMap<>();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createArticle(ArticleDTO article) {
        Long articleId = Math.abs(new Random().nextLong());
        article.setId(articleId);
        articles.put(articleId, article);
        return Response.created(URI.create(String.format("article/%s", article.getId()))).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticle(@PathParam("id") Long articleId) {
        if (articleId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if(articles.get(articleId) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response.ok(articles.get(articleId)).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editArticle(@PathParam("id") Long articleId, ArticleDTO article) {
        if (articleId == null || !articleId.equals(article.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if (articles.get(articleId) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            articles.put(articleId, article);
            return Response.ok(article).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public void deleteArticle(@PathParam("id") Long articleId) {
        articles.remove(articleId);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArticles(@QueryParam("page") @DefaultValue("1") int page,
                                @QueryParam("size") @DefaultValue("10") int size) {
        if (page < 1 || size < 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            var articles = this.articles.values().stream()
                    .skip(((page - 1L) * size))
                    .limit(size)
                    .collect(Collectors.toList());
            return Response.ok(articles).build();
        }
    }
}
