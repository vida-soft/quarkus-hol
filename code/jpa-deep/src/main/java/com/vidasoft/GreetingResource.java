package com.vidasoft;

import com.vidasoft.magman.model.Manager;

import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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