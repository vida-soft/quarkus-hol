package com.vidasoft.magman.model;

import javax.persistence.Entity;

@Entity
public class Author extends User {

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
