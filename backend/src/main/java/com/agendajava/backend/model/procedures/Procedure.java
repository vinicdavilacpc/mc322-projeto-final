package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;

import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;

public abstract class Procedure {
    private String name;
    private LocalDateTime startDateTime;
    private Duration duration;
    private Doctor doctorInCharge; // Médico responsável (principal médico envolvido)
    private Patient patient;

    public Procedure(String name, LocalDateTime time, Duration duration2, Patient patient, Doctor doctor) {
        this.name = name;
        this.startDateTime = time;
        this.duration = duration2;
        this.patient = patient;
        this.doctorInCharge = doctor;
    }

    public String getName() {
        return this.name;
    }

    public LocalDateTime getStarDateTime() {
        return this.startDateTime;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public Doctor getDoctor() {
        return this.doctorInCharge;
    }

    public LocalDateTime getEndDateTime() {
        return this.startDateTime.plus(this.duration);
    }

    public boolean overlapsWith(LocalDateTime start, Duration dur) {
        LocalDateTime thisEnd  = this.getEndDateTime();
        LocalDateTime otherEnd = start.plus(dur);

        // Dois intervalos se sobrepõe se o inicio de um for antes do final do outro
        return this.startDateTime.isBefore(otherEnd) && start.isBefore(thisEnd);
    }
}