package com.vidasoft.spendpal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreditCardDTO(
        @NotBlank
        @Pattern(regexp = "^\\d{16}$", message = "A credit card number should consist of 16 digits")
        String number,

        @NotNull
        CreditCardType type
) {}
