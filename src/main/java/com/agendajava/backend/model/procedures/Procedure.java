package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.agendajava.backend.model.users.Patient;

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

    public Procedure(String name, LocalDateTime startDateTime, Duration duration, Patient patient) { // TO DO: tornar o nome padronizado e automático
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

    public Duration getDuration() {
        return this.duration;
    }

    public Patient getPatient() {
        return this.patient;
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