package com.agendajava.backend.exceptions;

public class InvalidLogin extends RuntimeException {
    
    public InvalidLogin(String message) {
        super(message);
    }
}
