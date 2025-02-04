package com.example.demo.util;

public class ResponseWrapper {
    private int idMessage;
    private String message;
    private Object result;

    public ResponseWrapper(int idMessage, String message, Object result) {
        this.idMessage = idMessage;
        this.message = message;
        this.result = result;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }
}
