package com.vidasoft.spendpal;

import java.time.LocalDateTime;

public record ConfirmationDTO(boolean success, LocalDateTime timestamp) {
}
