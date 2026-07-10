package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;

import com.agendajava.backend.model.rooms.ExaminationRoom;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Examination extends Procedure {
    private ExaminationRoom room;

    @JsonCreator
    public Examination(
            @JsonProperty("name") String name, 
            @JsonProperty("starDateTime") LocalDateTime time, 
            @JsonProperty("duration") Duration duration, 
            @JsonProperty("patient") Patient patient, 
            @JsonProperty("doctor") Doctor doctor, 
            @JsonProperty("room") ExaminationRoom room) { // <-- Atributo extra!
        super(name, time, duration, patient, doctor);
        this.room = room;
    }
}
