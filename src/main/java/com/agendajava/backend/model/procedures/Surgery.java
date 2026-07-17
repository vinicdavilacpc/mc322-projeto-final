package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import com.agendajava.backend.model.Manager.Priority;
import com.agendajava.backend.model.Manager.Specialty;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Surgery extends Procedure {
    private Doctor doctor;
    int roomID;
    int blockTime;
    int estimatedDuration;
    int turnoverTime;
    int clinicalPriority;
    int estimatedRecoverTime;
    LocalDateTime limitDate;
    int surgeonID;
    Specialty specialty;
    List<String> necessaryEquipments;
    Priority priority;
    boolean icuNecessity;
    boolean anestesistNecessity;

    @JsonCreator
    public Surgery(
            @JsonProperty("name") String name, 
            @JsonProperty("starDateTime") LocalDateTime dateTime, 
            @JsonProperty("duration") Duration dur, 
            @JsonProperty("patient") Patient p, 
            @JsonProperty("doctor") Doctor d) {
        super(name, dateTime, dur, p);
        this.doctor = d;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }
}