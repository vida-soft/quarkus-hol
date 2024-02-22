package com.vidasoft.magman.model;

import jakarta.persistence.Entity;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Entity
public class Author extends User {

    public static final String ROLE_NAME = "AUTHOR";

    @Schema(example = "true")
    public boolean isRegular;

    @Schema(example = "1000000")
    public int salary;

    public Author() {
    }

    public Author(String userName, String password, String firstName, String lastName, String email, boolean isRegular, int salary) {
        super(userName, password, firstName, lastName, email);
        this.isRegular = isRegular;
        this.salary = salary;
    }
}
