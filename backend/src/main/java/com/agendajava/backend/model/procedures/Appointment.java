package com.agendajava.backend.model.procedures;

import java.time.LocalDateTime;

import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;

public class Appointment extends Procedure {
    private Doctor doctorInCharge;

    public Appointment(String name, LocalDateTime time, int durationInHours, Patient patient, Doctor doctorInCharge) {
        super(name, time, durationInHours, patient);
        this.doctorInCharge = doctorInCharge;
    }
}
