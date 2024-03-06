package com.vidasoft.magman.user;

import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class LoginDTO {

    @NotBlank
    @Schema(description = "The username the user used to create their account", example = "cj_123")
    private String userName;

    @NotBlank
    @Schema(description = "The password the user used to create their account", example = "Very$tr0ngPSW")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
