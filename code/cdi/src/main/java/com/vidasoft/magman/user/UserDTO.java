package com.vidasoft.magman.user;

import com.vidasoft.magman.model.User;

public class UserDTO {

    private String userName;
    private String firstName;
    private String lastName;
    private String email;


    public UserDTO() {
    }

    public UserDTO(User user) {
        userName = user.userName;
        firstName = user.firstName;
        lastName = user.lastName;
        email = user.email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
