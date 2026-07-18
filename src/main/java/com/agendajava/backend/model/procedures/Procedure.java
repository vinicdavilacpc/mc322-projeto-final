package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;

import com.agendajava.backend.model.users.Patient;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.PROPERTY, 
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Appointment.class, name = "appointment"),
    @JsonSubTypes.Type(value = Examination.class, name = "examination"),
    @JsonSubTypes.Type(value = Surgery.class, name = "surgery")
})
public abstract class Procedure {
    private String name;
    private LocalDateTime startDateTime;
    private Duration duration;
    private Patient patient;

    public Procedure(String name, LocalDateTime startDateTime, Duration duration, Patient patient) {
        this.name = name;
        this.startDateTime = startDateTime;
        this.duration = duration;
        this.patient = patient;
    }

    public String getName() {
        return this.name;
    }

    public LocalDateTime getStarDateTime() {
        return this.startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public Duration getDuration() {
        return this.duration;
    }

    public Patient getPatient() {
        return this.patient;
    }

    @JsonIgnore
    public LocalDateTime getEndDateTime() {
        if (this.startDateTime == null) return null;
        return this.startDateTime.plus(this.duration);
    }

    public boolean overlapsWith(LocalDateTime start, Duration dur) {
        LocalDateTime thisEnd  = this.getEndDateTime();
        LocalDateTime otherEnd = start.plus(dur);
        return this.startDateTime.isBefore(otherEnd) && start.isBefore(thisEnd);
    }
}