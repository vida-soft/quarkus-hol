package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.CreditCard;
import com.vidasoft.magman.model.CreditCardType;

public class CreditCardDTO {

    private String number;
    private CreditCardType type;

    public CreditCardDTO() {
    }

    public CreditCardDTO(CreditCard creditCard) {
        this.number = creditCard.number;
        this.type = creditCard.creditCardType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CreditCardType getType() {
        return type;
    }

    public void setType(CreditCardType type) {
        this.type = type;
    }
}
