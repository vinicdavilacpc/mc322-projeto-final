package com.agendajava.backend.exceptions;

public class WrongUser extends RuntimeException {
    
    public WrongUser(String message) {
        super(message);
    }
}
