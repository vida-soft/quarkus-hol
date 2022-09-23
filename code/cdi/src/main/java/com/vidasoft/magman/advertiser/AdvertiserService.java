package com.vidasoft.magman.advertiser;

import com.vidasoft.magman.model.Advertiser;
import com.vidasoft.magman.model.SponsorPackage;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class AdvertiserService {

    @Transactional
    public void createTestAdvertisers() {
        if (Advertiser.count() == 0) {
            new Advertiser("Google", "https://google.com", "advertiser@google.com", SponsorPackage.GOLD).persist();
            new Advertiser("Apple", "https://apple.com", "advertiser@apple.com", SponsorPackage.SILVER).persist();
            new Advertiser("Amazon", "https://amazon.com", "advertiser@amazon.com", SponsorPackage.BRONZE).persist();
        }
    }

}
