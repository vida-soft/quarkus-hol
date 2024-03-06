package com.vidasoft.magman.subscription;

import com.vidasoft.magman.model.CreditCard;
import com.vidasoft.magman.model.CreditCardType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class CreditCardDTO {

    @NotBlank
    @Schema(description = "The credit card number. Must consist of 16 digits", example = "1234567890123456")
    @Pattern(regexp = "^\\d{16,}$", message = "The credit card number must consist of at least 16 digits.")
    private String number;

    @NotNull
    @Schema(description = "The type of the credit card", example = "VISA")
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
