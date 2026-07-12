package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;

import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Appointment extends Procedure {
    private Doctor doctor;

    @JsonCreator
    public Appointment(
            @JsonProperty("name") String name, 
            @JsonProperty("starDateTime") LocalDateTime startDateTime, // Usa o nome exato do teu getter
            @JsonProperty("duration") Duration duration, 
            @JsonProperty("patient") Patient patient, 
            @JsonProperty("doctor") Doctor doctor) {
        super(name, startDateTime, duration, patient);
        this.doctor = doctor;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }
}
