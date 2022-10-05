package com.vidasoft.magman.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
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
