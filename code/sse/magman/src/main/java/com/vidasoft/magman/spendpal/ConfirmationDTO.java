package com.vidasoft.magman.spendpal;

import java.time.LocalDateTime;

public class ConfirmationDTO {
    private Boolean success;
    private LocalDateTime timestamp;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
