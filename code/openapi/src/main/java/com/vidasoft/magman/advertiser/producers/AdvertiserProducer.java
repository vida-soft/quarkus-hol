package com.vidasoft.magman.advertiser.producers;

import com.vidasoft.magman.model.Advertiser;
import com.vidasoft.magman.model.SponsorPackage;

import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import java.util.List;

public class AdvertiserProducer {

    @Gold
    @Produces
    @Dependent
    public List<Advertiser> produceGoldAdvertisers(InjectionPoint ctx) {
        var limit = ctx.getAnnotated().getAnnotation(Gold.class).limit();
        return getAdvertisers(Gold.SPONSOR_PACKAGE, limit);
    }

    @Silver
    @Produces
    @Dependent
    public List<Advertiser> produceSilverAdvertisers(InjectionPoint ctx) {
        var limit = ctx.getAnnotated().getAnnotation(Silver.class).limit();
        return getAdvertisers(Silver.SPONSOR_PACKAGE, limit);
    }

    @Bronze
    @Produces
    @Dependent
    public List<Advertiser> produceBronzeAdvertisers(InjectionPoint ctx) {
        var limit = ctx.getAnnotated().getAnnotation(Bronze.class).limit();
        return getAdvertisers(Bronze.SPONSOR_PACKAGE, limit);
    }

    private List<Advertiser> getAdvertisers(SponsorPackage sponsorPackage, int limit) {
        var query = Advertiser.<Advertiser>find("sponsorPackage = ?1", sponsorPackage);
        if (limit > 0) {
            query = query.page(0, limit);
        }

        return query.list();
    }

}
