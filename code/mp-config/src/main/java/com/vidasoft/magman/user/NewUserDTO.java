package com.vidasoft.magman.user;

import com.vidasoft.magman.validator.Password;

import jakarta.validation.constraints.NotNull;

public class NewUserDTO extends UserDTO {

    @Password
    private String password;

    @NotNull
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
