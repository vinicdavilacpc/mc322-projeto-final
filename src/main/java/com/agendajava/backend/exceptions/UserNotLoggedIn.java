package com.agendajava.backend.exceptions;

public class UserNotLoggedIn extends RuntimeException {
    
    public UserNotLoggedIn(String message) {
        super(message);
    }
}
