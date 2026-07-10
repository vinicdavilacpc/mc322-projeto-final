package com.agendajava.backend.exceptions;

public class SchedulingConflictException extends RuntimeException {

    public SchedulingConflictException(String mensagem) {
        super(mensagem);
    }
}
