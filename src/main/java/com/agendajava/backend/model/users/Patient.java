package com.agendajava.backend.model.users;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TreeMap;

import com.agendajava.backend.exceptions.SchedulingConflict;
import com.agendajava.backend.model.procedures.Procedure;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Patient extends User {

    @JsonCreator 
    public Patient(@JsonProperty("name") String name, 
                   @JsonProperty("email") String email, 
                   @JsonProperty("password") String password) {
        super(name, email, password);
    }
}
