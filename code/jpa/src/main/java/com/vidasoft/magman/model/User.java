package com.vidasoft.magman.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public abstract class User extends AbstractEntity {
    public String userName;
    public String password;
    public String firstName;
    public String lastName;
    public String email;

    public User() {
    }

    public User(String userName, String password, String firstName, String lastName, String email) {
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
