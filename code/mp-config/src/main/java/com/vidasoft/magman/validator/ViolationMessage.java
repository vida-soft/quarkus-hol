package com.vidasoft.magman.validator;

public class ViolationMessage {

    private String property;
    private String message;

    public ViolationMessage() {
    }

    public ViolationMessage(String property, String message) {
        this.property = property;
        this.message = message;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
