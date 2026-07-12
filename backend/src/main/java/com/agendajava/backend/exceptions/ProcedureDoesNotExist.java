package com.agendajava.backend.exceptions;

public class ProcedureDoesNotExist extends RuntimeException {

    public ProcedureDoesNotExist(String message) {
        super(message);
    }
}
