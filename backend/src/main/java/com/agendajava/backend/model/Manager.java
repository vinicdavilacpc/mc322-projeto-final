package com.agendajava.backend.model;

import java.time.LocalDateTime;
import java.time.Duration;

import com.agendajava.backend.exceptions.InvalidLogin;
import com.agendajava.backend.exceptions.SchedulingConflict;
import com.agendajava.backend.exceptions.UserAlreadyExists;
import com.agendajava.backend.model.procedures.Appointment;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import com.agendajava.backend.model.users.User;

public class Manager {
    private User user;
    private DataManager dataManager;
    private Authenticator authenticator;

    public Manager() {
        this.dataManager = new DataManager();
        this.authenticator = new Authenticator();
        this.user = null;
    }

    public boolean loginSuccessful(String email, String password) {
        try {
            this.user = authenticator.login(email, password, dataManager);
        } 
        catch (InvalidLogin e) {
            return false;
        }
        return true;
    }

    public boolean registrationSuccessful(String name, String email, String password, String role, DataManager dataManager) {
        try {
            this.user = authenticator.register(name, email, password, role, dataManager);
        }
        catch (UserAlreadyExists e) {
            return false;
        }
        return true;
    }   

    // confiamos que sempre vai ser um paciente
    public boolean appointmentCreated(String name, LocalDateTime startDateTime, Duration duration, Doctor doctor) {
        Patient patient = (Patient) this.user;
        Appointment appointment = new Appointment(name, startDateTime, duration, patient, doctor);
        try {
            patient.schedule(startDateTime, duration, appointment);
        } 
        catch (SchedulingConflict e) {
            return false;
        }
        try {
            doctor.schedule(startDateTime, duration, appointment);
        } 
        catch (SchedulingConflict e) {
            return false;
        }
        // ATUALIZAR PACIENTE
        // ATUALIZAR DOUTOR
        this.dataManager.add(this.dataManager.getProceduresFile(), appointment);
        return true;
    }

    // confiamos que sempre vai ser um pacient
    public boolean examinationCreated(String name, LocalDateTime startDateTime, Duration duration, Doctor doctor) {
        Patient patient = (Patient) this.user;
        Appointment appointment = new Appointment(name, startDateTime, duration, patient, doctor);
        try {
            patient.schedule(startDateTime, duration, appointment);
        } 
        catch (SchedulingConflict e) {
            return false;
        }
        try {
            doctor.schedule(startDateTime, duration, appointment);
        } 
        catch (SchedulingConflict e) {
            return false;
        }
        // ATUALIZAR PACIENTE
        // ATUALIZAR DOUTOR
        this.dataManager.add(this.dataManager.getProceduresFile(), appointment);
        return true;
    }

}

