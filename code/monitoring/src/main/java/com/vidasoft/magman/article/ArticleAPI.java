package com.vidasoft.magman.article;

import com.vidasoft.magman.model.Author;
import com.vidasoft.magman.model.Manager;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
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
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;

@Authenticated
@Path("/article")
@Tag(name = "Article Resource", description = "Contains all the endpoints, required to create, update and delete articles.")
public interface ArticleAPI {
    @POST
    @RolesAllowed({Author.ROLE_NAME})
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "createArticle",
            summary = "Create article",
            description = "Creates an article"
    )
    @RequestBody(
            name = "articleDTO",
            description = "The article that is going to be created",
            content = @Content(
                    schema = @Schema(
                            implementation = ArticleDTO.class,
                            example = """
                                    {
                                        "title": "Article for the soul.",
                                        "content": "The quick brown fox runs over the lazy dog."        
                                    }
                                    """
                    )
            )
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "201",
                    name = "Article created",
                    description = "Successfully created the article",
                    headers = @Header(
                            name = "location",
                            description = "The get URI for the newly created article, including its id",
                            schema = @Schema(
                                    implementation = String.class,
                                    example = "http://localhost:8080/article/10"
                            )
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    name = "Bad Request",
                    description = "You provided malformed query parameters or the User type is not Author. Check the requirements and try again."
            ),
            @APIResponse(
                    responseCode = "401",
                    name = "Unauthorized",
                    description = "You need to provide Bearer token in the authentication header or your token has expired.\n" +
                            "Generate a new token, using the Login resource."
            ),
            @APIResponse(
                    responseCode = "403",
                    name = "Not allowed",
                    description = """
                            The user you are accessing this endpoint with, has no permission to access it.
                            """
            )
    })
    Response createArticle(@Valid @NotNull ArticleDTO articleDTO);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getArticle",
            summary = "Get article",
            description = "Gets article by its id"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "The found article",
                    description = "Successfully created the article",
                    content = @Content(
                            schema = @Schema(implementation = ArticleDTO.class)
                    )
            ),
            @APIResponse(
                    responseCode = "401",
                    name = "Unauthorized",
                    description = "You need to provide Bearer token in the authentication header or your token has expired.\n" +
                            "Generate a new token, using the Login resource."
            ),
            @APIResponse(
                    responseCode = "404",
                    name = "Not Found",
                    description = "The article you are looking for cannot be found."
            )
    })
    Response getArticle(@Parameter(required = true) @Positive @PathParam("id") Long articleId);

    @PUT
    @Path("/{id}")
    @RolesAllowed({Author.ROLE_NAME})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "editArticle",
            summary = "Edit article",
            description = "Updates an article by its id"
    )
    @RequestBody(
            name = "articleDTO",
            description = "The article that is going to be edited. The id in the body must match the id in the request",
            content = @Content(
                    schema = @Schema(
                            implementation = ArticleDTO.class,
                            example = """
                                    {
                                        "id": 3,
                                        "title": "Article for the soul.",
                                        "content": "The quick brown fox runs over the lazy dog."
                                    }
                                    """
                    )
            )
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "Article edited",
                    description = "The article was updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = ArticleDTO.class),
                            example = """
                                    {
                                      "id": 3,
                                      "title": "Article for the soul.",
                                      "content": "The quick brown fox runs over the lazy dog.",
                                      "publishDate": "2022-09-13T00:00",
                                      "lastModified": "2022-09-13T00:01",
                                      "authorId": 256,
                                      "comments": [
                                        {
                                          "id": 124,
                                          "content": "I loved this article. It is true that dogs are lazy when foxes are quick.",
                                          "authorId": 235,
                                          "created": "2022-09-13T00:01"
                                        }
                                      ]
                                    }
                                    """
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    name = "Bad Request",
                    description = "You provided malformed query parameters or the User type is not Author. Check the requirements and try again."
            ),
            @APIResponse(
                    responseCode = "401",
                    name = "Unauthorized",
                    description = "You need to provide Bearer token in the authentication header or your token has expired.\n" +
                            "Generate a new token, using the Login resource."
            ),
            @APIResponse(
                    responseCode = "403",
                    name = "Not allowed",
                    description = """
                            The user you are accessing this endpoint with, has no permission to access it.
                            """
            )
    })
    Response editArticle(@Parameter(description = "Must natch the article id in the body.", example = "3")
                         @Positive @PathParam("id") Long articleId, @Valid @NotNull ArticleDTO articleDTO);

    @DELETE
    @Path("/{id}")
    @RolesAllowed({Author.ROLE_NAME, Manager.ROLE_NAME})
    @Operation(
            operationId = "deleteArticle",
            summary = "Delete article",
            description = "Permanently removes an article and its related comments by its id"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "The article was successfully removed"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "You must provide valid articleId"
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Authorization header missing or expired"
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "The article cannot be removed by this user. You must be the author of the article or system admin"
            )
    })
    void deleteArticle(@Positive @PathParam("id") Long articleId);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId = "getArticles",
            summary = "Get articles",
            description = "Returns a list of articles. Can be filtered by author's id"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    name = "List of articles",
                    description = "The articles that were found",
                    content = @Content(
                            schema = @Schema(name = "ArticleDTO",
                                    implementation = ArticleDTO[].class,
                                    description = "List of found articles, filtered by author (if provided).",
                                    example = """
                                            [
                                               {
                                                   "authorId": 1,
                                                   "content": "The quick brown fox runs over the lazy dog.",
                                                   "id": 3,
                                                   "publishDate": "2022-01-12T00:00",
                                                   "title": "Article for the soul."
                                                 },
                                                 {
                                                   "authorId": 1,
                                                   "content": "This is an article by the same author, who created Ipsum Lorem",
                                                   "id": 4,
                                                   "publishDate": "2022-02-12T00:00",
                                                   "title": "The aitor that created"
                                                 },
                                                 {
                                                   "authorId": 2,
                                                   "content": "This is how I got my hands into Java long time ago. Long article here...",
                                                   "id": 5,
                                                   "publishDate": "2020-01-10T00:00",
                                                   "title": "The way I became Java developer"
                                                 },
                                                 {
                                                   "authorId": 2,
                                                   "content": "This is my extreme enjoyment of Quarkus, written in an article",
                                                   "id": 6,
                                                   "publishDate": "2022-09-13T00:00",
                                                   "title": "I love Quarkus and Quarkus loves me back"
                                                 }
                                            ]
                                            """
                            )
                    )
            ),
            @APIResponse(
                    responseCode = "400",
                    name = "Bad Request",
                    description = "You provided malformed query parameters. Check the requirements and try again."
            ),
            @APIResponse(
                    responseCode = "401",
                    name = "Unauthorized",
                    description = """
                                    You need to provide Bearer token in the authentication header or your token has expired.
                                    Generate a new token, using the Login resource
                            """
            ),
            @APIResponse(
                    responseCode = "403",
                    name = "Not allowed",
                    description = """
                            The user you are accessing this endpoint with, has no permission to access it.
                            """
            )
    })
    @Parameter(
            name = "author",
            in = ParameterIn.QUERY,
            description = "Filter articles by author id. If empty, will return all articles",
            example = "123"
    )
    Response getArticles(@Parameter(required = true, description = "Search result page, starts from 1") @QueryParam("page") @DefaultValue("1") @Positive int page,
                         @Parameter(required = true, description = "Size of the search result page. Cannot be 0") @QueryParam("size") @DefaultValue("10") @Positive int size,
                         @QueryParam("author") @Positive Long authorId);

    @PATCH
    @RolesAllowed({Manager.ROLE_NAME})
    @Path("{id}/advertiser/{advertiserId}")
    @Operation(
            operationId = "addAdvertiserToArticle",
            summary = "Promote article",
            description = "Adds advertiser to the article"
    )
    @APIResponses({
            @APIResponse(
                    responseCode = "204",
                    description = "The advertiser was added to the article"
            ),
            @APIResponse(
                    responseCode = "400",
                    description = "The article id or the advertiser id are malformed"
            ),
            @APIResponse(
                    responseCode = "401",
                    description = "Authorization header missing or expired"
            ),
            @APIResponse(
                    responseCode = "403",
                    description = "The operation cannot be done with this user role"
            )
    })
    Response addAdvertiserToArticle(@Positive @PathParam("id") Long id, @Positive @PathParam("advertiserId") Long advertiserId);
}
