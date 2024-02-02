package com.vidasoft.magman.advertiser;

import com.vidasoft.magman.advertiser.decorators.AdvertiserMapper;
import com.vidasoft.magman.model.Advertiser;
import io.quarkus.security.Authenticated;
import jakarta.annotation.PostConstruct;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
@Authenticated
@Path("advertiser")
public class AdvertiserResource {

    @Inject
    AdvertiserService advertiserService;

    @Inject
    AdvertiserMapper advertiserMapper;

    @PostConstruct
    void init() {
        advertiserService.createTestAdvertisers();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AdvertiserDTO> getAllAdvertisers() {
        return Advertiser.<Advertiser>streamAll().map(advertiserMapper::toAdvertiserDTO).collect(Collectors.toList());
    }

}
