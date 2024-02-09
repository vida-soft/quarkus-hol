package com.vidasoft.spendpal.dto;

import java.time.LocalDateTime;

public record ConfirmationDTO(boolean success, LocalDateTime timestamp) {
}
