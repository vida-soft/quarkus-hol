package com.vidasoft.magman.advertiser;

import com.vidasoft.magman.advertiser.decorators.AdvertiserMapper;
import com.vidasoft.magman.model.Advertiser;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class AdvertiserResource implements AdvertiserAPI {

    @Inject
    AdvertiserService advertiserService;

    @Inject
    AdvertiserMapper advertiserMapper;

    @PostConstruct
    public void init() {
        advertiserService.createTestAdvertisers();
    }

    @Override
    public List<AdvertiserDTO> getAllAdvertisers() {
        return Advertiser.<Advertiser>streamAll().map(advertiserMapper::toAdvertiserDTO).collect(Collectors.toList());
    }

}
