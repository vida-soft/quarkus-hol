package com.vidasoft.magman.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

@Entity
public class Advertiser extends AbstractEntity {

    public String name;

    public String website;

    public String contactEmail;

    @Lob
    public byte[] logo;

    @Enumerated(EnumType.STRING)
    public SponsorPackage sponsorPackage;

}
