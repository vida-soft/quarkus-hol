package com.vidasoft.magman.user;

import com.vidasoft.magman.model.Author;

import jakarta.persistence.EntityManager;
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

    EntityManager entityManager;

    public AuthorResource(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createAuthor(AuthorDTO authorDTO) {

        Author author = new Author(authorDTO.getUserName(), authorDTO.getPassword(), authorDTO.getFirstName(),
                authorDTO.getLastName(), authorDTO.getEmail(), authorDTO.isRegular(), authorDTO.getSalary());
        entityManager.persist(author);

        return Response.created(URI.create(String.format("/user/author/%d",author.id))).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthor(@PathParam("id") Long authorId) {
        if (authorId < 1 || authorId < 0) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            Author author = entityManager.find(Author.class, authorId);
            if (author == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                return Response.ok(new AuthorDTO(author)).build();
            }
        }
    }
}
