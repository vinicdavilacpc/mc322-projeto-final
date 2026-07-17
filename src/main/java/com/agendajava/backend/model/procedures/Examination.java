package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;

import com.agendajava.backend.model.rooms.ExaminationRoom;
import com.agendajava.backend.model.users.Patient;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Examination extends Procedure {
    private final ExaminationRoom room;

    public ExaminationRoom getRoom() {
        return this.room;
    }

    @JsonCreator
    public Examination(
            @JsonProperty("name") String name, 
            @JsonProperty("starDateTime") LocalDateTime time, 
            @JsonProperty("duration") Duration duration, 
            @JsonProperty("patient") Patient patient, 
            @JsonProperty("room") ExaminationRoom room) { // <-- Atributo extra!
        super(name, time, duration, patient);
        this.room = room;
    }

    public ExaminationRoom getRoom() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
