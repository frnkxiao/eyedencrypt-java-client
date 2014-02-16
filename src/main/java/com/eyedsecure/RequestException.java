package com.eyedsecure;


public class RequestException extends Exception {
    private static final long serialVersionUID = 1L;

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(String message) {
        super(message);
    }
}