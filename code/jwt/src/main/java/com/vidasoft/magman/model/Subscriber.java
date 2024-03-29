package com.vidasoft.magman.model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Subscriber extends User {

    public static final String ROLE_NAME = "SUBSCRIBER";

    public String streetAddress;
    public LocalDate subscribedUntil;

    @Embedded
    public CreditCard creditCard;

    public Subscriber() {
    }

    public Subscriber(String userName, String password, String firstName, String lastName, String email, String streetAddress, LocalDate subscribedUntil, CreditCard creditCard) {
        super(userName, password, firstName, lastName, email);
        this.streetAddress = streetAddress;
        this.subscribedUntil = subscribedUntil;
        this.creditCard = creditCard;
    }
}
