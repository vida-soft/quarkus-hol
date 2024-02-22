package com.vidasoft.magman.user;

import com.vidasoft.magman.model.Author;
import jakarta.ws.rs.core.Response;

public class AuthorResource implements AuthorAPI {

    @Override
    public Response getAuthor(Long authorId) {
        if (authorId < 1) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            return Author.<Author>findByIdOptional(authorId)
                    .map(author -> Response.ok(new AuthorDTO(author)).build())
                    .orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
        }
    }
}
