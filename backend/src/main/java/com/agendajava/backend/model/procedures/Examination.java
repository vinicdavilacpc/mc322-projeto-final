package com.agendajava.backend.model.procedures;

import java.time.LocalDateTime;

import com.agendajava.backend.model.rooms.ExaminationRoom;
import com.agendajava.backend.model.users.Patient;

public class Examination extends Procedure {
    private ExaminationRoom room;

    public Examination(String name, LocalDateTime time, int durationInHours, Patient patient, ExaminationRoom room) {
        super(name, time, durationInHours, patient);
        this.room = room;
    }
}
