package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;

import com.agendajava.backend.model.rooms.ExaminationRoom;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;

public class Examination extends Procedure {
    private ExaminationRoom room;

    public Examination(String name, LocalDateTime time, Duration duration, Patient patient, Doctor doctor, ExaminationRoom room) {
        super(name, time, duration, patient, doctor);
        this.room = room;
    }
}
