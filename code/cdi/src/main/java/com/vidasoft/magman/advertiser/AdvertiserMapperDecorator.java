package com.vidasoft.magman.advertiser;

import com.vidasoft.magman.model.Advertiser;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import java.util.Base64;

@Decorator
public abstract class AdvertiserMapperDecorator implements AdvertiserMapper {

    @Inject
    @Delegate
    AdvertiserMapper advertiserMapper;

    @Override
    public AdvertiserDTO toAdvertiserDTO(Advertiser advertiser) {
        var dto = advertiserMapper.toAdvertiserDTO(advertiser);
        dto.setLogo(getBase64Image(advertiser.logo));
        return dto;
    }

    private String getBase64Image(byte[] image) {
        if (image != null) {
            return Base64.getEncoder().encodeToString(image);
        } return null;
    }
}
