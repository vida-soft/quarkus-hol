package com.vidasoft.magman.model;

public enum CreditCardType {

    VISA("Visa"), MASTER_CARD("MasterCard"), AMERICAN_EXPRESS("American Express");

    private String displayName;

    CreditCardType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
