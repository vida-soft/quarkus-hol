package com.vidasoft.magman.model;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
public class CreditCard implements Serializable {

    public static final CreditCard DEFAULT = new CreditCard("", CreditCardType.VISA);

    public String number;

    @Enumerated(EnumType.STRING)
    public CreditCardType creditCardType;

    public CreditCard() {
    }

    public CreditCard(String number, CreditCardType creditCardType) {
        this.number = number;
        this.creditCardType = creditCardType;
    }
}
