package com.vidasoft.magman.user;

import com.vidasoft.magman.model.Subscriber;
import com.vidasoft.magman.model.User;
import com.vidasoft.magman.validator.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDate;

public class UserDTO {

    @Schema(description = "The database id of the user", example = "12")
    private Long id;

    @Unique
    @NotBlank
    @Schema(description = "The unique username of the user", example = "cj_123")
    private String userName;

    @NotBlank
    @Schema(description = "First Name", example = "Cave")
    private String firstName;

    @NotBlank
    @Schema(description = "Last Name", example = "Johnson")
    private String lastName;

    @Email
    @Schema(description = "Email", example = "cave@contoso.com")
    private String email;

    @Schema(description = "The period of the user subscription", example = "2022-03-10")
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
