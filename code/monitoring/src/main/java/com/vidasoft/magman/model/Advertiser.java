package com.vidasoft.magman.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import java.util.Set;

@Entity
public class Advertiser extends AbstractEntity {

    public String name;

    public String website;

    public String contactEmail;

    @ManyToMany(mappedBy = "advertisers")
    public Set<Article> articles;

    @Lob
    public byte[] logo;

    @Enumerated(EnumType.STRING)
    public SponsorPackage sponsorPackage;

    public Advertiser() {
    }

    public Advertiser(String name, String website, String contactEmail, SponsorPackage sponsorPackage) {
        this.name = name;
        this.website = website;
        this.contactEmail = contactEmail;
        this.sponsorPackage = sponsorPackage;
    }
}
