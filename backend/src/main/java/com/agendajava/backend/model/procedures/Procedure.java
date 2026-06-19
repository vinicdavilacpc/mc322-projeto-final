package com.agendajava.backend.model.procedures;

import java.time.LocalDateTime;

import com.agendajava.backend.model.users.Patient;

public abstract class Procedure {
    private String name;
    private LocalDateTime time;
    private int durationInHours;
    private Patient patient;

    public Procedure(String name, LocalDateTime time, int durationInHours, Patient patient) {
        this.name = name;
        this.time = time;
        this.durationInHours = durationInHours;
        this.patient = patient;
    }
}