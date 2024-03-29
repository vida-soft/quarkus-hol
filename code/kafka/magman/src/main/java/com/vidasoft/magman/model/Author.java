package com.vidasoft.magman.model;

import jakarta.persistence.Entity;

@Entity
public class Author extends User {

    public static final String ROLE_NAME = "AUTHOR";

    public boolean isRegular;
    public int salary;

    public Author() {
    }

    public Author(String userName, String password, String firstName, String lastName, String email, boolean isRegular, int salary) {
        super(userName, password, firstName, lastName, email);
        this.isRegular = isRegular;
        this.salary = salary;
    }
}
