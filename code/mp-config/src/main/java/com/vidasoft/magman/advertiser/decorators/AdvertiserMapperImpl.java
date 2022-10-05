package com.vidasoft.magman.advertiser.decorators;

import com.vidasoft.magman.advertiser.AdvertiserDTO;
import com.vidasoft.magman.model.Advertiser;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AdvertiserMapperImpl implements AdvertiserMapper {

    public AdvertiserDTO toAdvertiserDTO(Advertiser advertiser) {
        return new AdvertiserDTO(advertiser);
    }

}
