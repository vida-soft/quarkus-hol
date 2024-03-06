package com.vidasoft.magman.user;

import com.vidasoft.magman.validator.Password;

import jakarta.validation.constraints.NotNull;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

public class NewUserDTO extends UserDTO {

    @Password
    @Schema(description = "Complicated password for the user", example = "Very$tr0ngPSW")
    private String password;

    @NotNull
    @Schema(description = "The type of the user will decide what access they have", example = "SUBSCRIBER")
    private UserType userType;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
