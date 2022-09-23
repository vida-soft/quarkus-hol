package com.vidasoft.magman.advertiser;

import com.vidasoft.magman.model.Advertiser;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AdvertiserMapperImpl implements AdvertiserMapper {

    public AdvertiserDTO toAdvertiserDTO(Advertiser advertiser) {
        return new AdvertiserDTO(advertiser);
    }

}
