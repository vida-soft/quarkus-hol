package com.vidasoft.magman.advertiser.decorators;

import com.vidasoft.magman.advertiser.AdvertiserDTO;
import com.vidasoft.magman.model.Advertiser;

public interface AdvertiserMapper {

    AdvertiserDTO toAdvertiserDTO(Advertiser advertiser);

}
