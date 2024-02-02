package com.vidasoft.magman.user;

import com.vidasoft.magman.model.Author;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("/user/author")
public class AuthorResource {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthor(@PathParam("id") Long authorId) {
        if (authorId < 1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Author.<Author>findByIdOptional(authorId)
                    .map(author -> Response.ok(new AuthorDTO(author)).build())
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
        }
    }
}
