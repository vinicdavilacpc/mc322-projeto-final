package com.agendajava.backend.model.users;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TreeMap;

import com.agendajava.backend.exceptions.SchedulingConflict;
import com.agendajava.backend.interfaces.Schedulable;
import com.agendajava.backend.model.Manager.Specialty;
import com.agendajava.backend.model.procedures.Procedure;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Doctor extends User {
    Specialty specialty; // Especialidade do médico
    boolean surgeon;     // Diz se um médico é cirurgião de sua especialidade (true) ou não (false)

    @JsonCreator
    public Doctor(@JsonProperty("name") String name, 
                  @JsonProperty("email") String email, 
                  @JsonProperty("password") String password,
                  @JsonProperty("specialty") Specialty specialty,
                  @JsonProperty("surgeon") boolean surgeon) {
        super(name, email, password);
        this.specialty = specialty;
        this.surgeon = surgeon;
    }

    public Specialty getSpecialty() {
        return this.specialty;
    }

    public boolean isSurgeon() {
        return this.surgeon;
    }
}
