package com.vidasoft.magman.user;

import com.vidasoft.magman.model.Author;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/user/author")
public class AuthorResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createAuthor(AuthorDTO authorDTO) {

        Author author = new Author(authorDTO.getUserName(), authorDTO.getPassword(), authorDTO.getFirstName(),
                authorDTO.getLastName(), authorDTO.getEmail(), authorDTO.isRegular(), authorDTO.getSalary());
        author.persist();

        return Response.created(URI.create(String.format("/user/author/%d",author.id))).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthor(@PathParam("id") Long authorId) {
        if (authorId == null || authorId < 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Author.<Author>findByIdOptional(authorId)
                    .map(author -> Response.ok(new AuthorDTO(author)).build())
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
        }
    }
}
