package com.vidasoft.magman.advertiser;

import com.vidasoft.magman.advertiser.decorators.AdvertiserMapper;
import com.vidasoft.magman.model.Advertiser;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
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
