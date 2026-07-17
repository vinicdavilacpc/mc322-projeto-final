package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.agendajava.backend.model.Manager.Priority;
import com.agendajava.backend.model.Manager.Specialty;
import com.agendajava.backend.model.rooms.SurgeryRoom;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Surgery extends Procedure {
    private Doctor surgeon;              
    private SurgeryRoom room;            
    
    private Specialty specialty;  
    private Priority priority;    
    private boolean icuNecessity; 

    public static final Duration TURNOVER_TIME = Duration.ofMinutes(30); 

    private int clinicalPriority;                          
    private Duration estimatedRecoverDuration;             
    private LocalDate limitDate;                           

    @JsonCreator
    public Surgery (
            @JsonProperty("name") String name, 
            @JsonProperty("patient") Patient patient,
            @JsonProperty("specialty") Specialty specialty,
            @JsonProperty("priority") Priority priority,
            @JsonProperty("icuNecessity") boolean icuNecessity,
            @JsonProperty("estimatedDuration") Duration duration,
            @JsonProperty("clinicalPriority") int clinicalPriority,
            @JsonProperty("estimatedRecoverDuration") Duration estimatedRecoverDuration,
            @JsonProperty("limitDate") LocalDate limitDate) {
        
        super(name, null, duration.plus(TURNOVER_TIME), patient);
        this.specialty = specialty;
        this.priority = priority;
        this.icuNecessity = icuNecessity;
        this.clinicalPriority = clinicalPriority;
        this.estimatedRecoverDuration = estimatedRecoverDuration;
        this.limitDate = limitDate;
    }

    public boolean isEmergency() {
        return priority == Priority.EMERGENCIA;
    }

    public boolean isUrgency() {
        return priority == Priority.URGENCIA;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public LocalDate getLimitDate() {
        return limitDate;
    }

    public boolean needsICU() {
        return icuNecessity;
    }

    public Duration getICURecoverTime() {
        return estimatedRecoverDuration;
    }

    public int getClinicalPriority() {
        return clinicalPriority;
    }

    public Doctor getDoctor() {
        return this.surgeon;
    }

    public void setSurgeon(Doctor surgeon) {
        this.surgeon = surgeon;
    }

    public void setRoom(SurgeryRoom room) {
        this.room = room;
    }

    public void setStart(LocalDateTime startDateTime) {
        this.setStartDateTime(startDateTime);
    }
}