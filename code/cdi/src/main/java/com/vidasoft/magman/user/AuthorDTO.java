package com.vidasoft.magman.user;

import com.vidasoft.magman.model.Author;

public class AuthorDTO extends UserDTO {

    private boolean isRegular;
    private int salary;

    public AuthorDTO() {
    }

    public AuthorDTO(Author author) {
        super(author);
        isRegular = author.isRegular;
        salary = author.salary;
    }

    public boolean isRegular() {
        return isRegular;
    }

    public void setRegular(boolean regular) {
        isRegular = regular;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }
}
