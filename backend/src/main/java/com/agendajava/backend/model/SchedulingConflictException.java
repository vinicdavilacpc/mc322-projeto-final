package com.agendajava.backend.model;

public class SchedulingConflictException extends RuntimeException {

    public SchedulingConflictException(String mensagem) {
        super(mensagem);
    }
}
