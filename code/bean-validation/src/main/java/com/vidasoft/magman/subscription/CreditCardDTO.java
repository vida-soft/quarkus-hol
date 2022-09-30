package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.CreditCard;
import com.vidasoft.magman.model.CreditCardType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CreditCardDTO {

    @NotBlank
    @Pattern(regexp = "^\\d{16,}$", message = "The credit card number must consist of at least 16 digits.")
    private String number;

    @NotNull
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
