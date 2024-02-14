package com.vidasoft.magman.user;

import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.model.User;
import com.vidasoft.magman.validator.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class UserDTO {

    private Long id;

    @Unique
    @NotBlank
    private String userName;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    private LocalDate subscribedUntil;

    public UserDTO() {
    }

    public UserDTO(User user) {
        id = user.id;
        userName = user.userName;
        firstName = user.firstName;
        lastName = user.lastName;
        email = user.email;

        if (user instanceof Subscriber subscriber) {
            subscribedUntil = subscriber.subscribedUntil;
        }
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getSubscribedUntil() {
        return subscribedUntil;
    }

    public void setSubscribedUntil(LocalDate subscribedUntil) {
        this.subscribedUntil = subscribedUntil;
    }
}
