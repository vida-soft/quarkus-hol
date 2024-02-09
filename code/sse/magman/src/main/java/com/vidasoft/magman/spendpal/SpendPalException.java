package com.vidasoft.magman.spendpal;

public class SpendPalException extends Exception {

    private int statusCode;
    private Object body;

    public SpendPalException(int statusCode, Object body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Object getBody() {
        return body;
    }
}
