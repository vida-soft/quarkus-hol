package com.vidasoft;

import com.vidasoft.magman.model.Manager;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String hello() {
        Manager manager = new Manager("john", "3volta", "John", "Trivolta", "john@google.com");
        return "Hello RESTEasy";
    }
}