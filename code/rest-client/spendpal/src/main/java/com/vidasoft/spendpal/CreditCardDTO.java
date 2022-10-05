package com.vidasoft.spendpal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record CreditCardDTO(
        @NotBlank
        @Pattern(regexp = "^\\d{16}$", message = "A credit card number should consist of 16 digits")
        String number,

        @NotNull
        CreditCardType type
) {}
