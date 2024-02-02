package com.vidasoft.magman.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Subscriber extends User {

    public String streetAddress;
    public LocalDate subscribedUntil;

    @Embedded
    public CreditCard creditCard;

}
