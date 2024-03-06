package com.vidasoft.magman.advertiser;

import com.vidasoft.magman.model.Advertiser;
import com.vidasoft.magman.model.SponsorPackage;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class AdvertiserDTO {

    @Schema(description = "The id of the advertiser", example = "12")
    private Long id;

    @Schema(description = "The name of the advertiser", example = "Apperture Science")
    private String name;

    @Schema(description = "Advertiser website", example = "https://www.aperturescience.com")
    private String website;

    @Schema(description = "The email address of the advertising company or the contact person", example = "cave@.aperturescience.com")
    private String contactEmail;

    @Schema(description = "Encoded bytes used for advertiser logo")
    private String logo;

    @Schema(description = "The sponsor package this advertiser has paid to add them to the article", example = "GOLD")
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
