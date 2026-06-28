package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;

import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;

public class Appointment extends Procedure {

    public Appointment(String name, LocalDateTime time, Duration duration, Patient patient, Doctor doctor) {
        super(name, time, duration, patient, doctor);
    }
}
