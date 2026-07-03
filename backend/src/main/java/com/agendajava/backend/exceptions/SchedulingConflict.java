package com.agendajava.backend.exceptions;

public class SchedulingConflict extends RuntimeException {
    public SchedulingConflict(String message) {
        super(message);
    }
}
