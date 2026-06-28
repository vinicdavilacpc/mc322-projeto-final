package com.agendajava.backend.model.users;

import java.time.Duration;
import java.time.LocalDateTime;

import com.agendajava.backend.interfaces.Schedulable;
import com.agendajava.backend.model.procedures.Procedure;

public class Doctor extends User implements Schedulable {
    String specialty;

    public Doctor(String name, String email, String password, String specialty) {
        super(name, email, password);
        this.specialty = specialty;
    }

    public boolean isAvailable(LocalDateTime startDateTime, Duration duration) {
        return true;
    }
    public void schedule(LocalDateTime startDateTime, Duration duration, Procedure procedure) {}
}
