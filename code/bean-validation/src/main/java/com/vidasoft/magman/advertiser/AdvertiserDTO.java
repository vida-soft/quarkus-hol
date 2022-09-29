package com.vidasoft.magman.advertiser;

import com.vidasoft.magman.model.Advertiser;
import com.vidasoft.magman.model.SponsorPackage;

public class AdvertiserDTO {

    private Long id;
    private String name;
    private String website;
    private String contactEmail;
    private String logo;
    private SponsorPackage sponsorPackage;

    public AdvertiserDTO() {
    }

    public AdvertiserDTO(Advertiser advertiser) {
        id = advertiser.id;
        name = advertiser.name;
        website = advertiser.website;
        contactEmail = advertiser.contactEmail;
        sponsorPackage = advertiser.sponsorPackage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public SponsorPackage getSponsorPackage() {
        return sponsorPackage;
    }

    public void setSponsorPackage(SponsorPackage sponsorPackage) {
        this.sponsorPackage = sponsorPackage;
    }
}
