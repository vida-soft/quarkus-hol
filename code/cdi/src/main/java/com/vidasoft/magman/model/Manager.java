package com.vidasoft.magman.model;

import javax.persistence.Entity;

@Entity
public class Manager extends User {

    public Manager() {
    }

    public Manager(String userName, String password, String firstName, String lastName, String email) {
        super(userName, password, firstName, lastName, email);
    }
}
