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

    public boolean appointmentCreated(String name, LocalDateTime startDateTime, Duration duration, Doctor doctor) {
        Patient patient = (Patient) this.user;
        Appointment appointment = new Appointment(name, startDateTime, duration, patient, doctor);

        if (this.user == null || !(this.user instanceof Patient)) {
            System.out.println("Only patients can schedule appointments.");
            return false;
        }

        // precisa verificar se patient, doctor e rooms isAvailable antes de schedule, pq mesmo q tenha essa verificação no shedule,
        // pode ser que um médico esteja livre mas a sala não e salvaria a consulta no calendario
        if (!patient.isAvailable(startDateTime, duration)) {
            System.out.println("You already have an appointment scheduled at that time");
        }
        if (!doctor.isAvailable(startDateTime, duration)) {
            System.out.println("Doctor" + doctor.getName() + "isn't available at that time.");
        }

        // assim pode ser um unico try-catch
        try {
            patient.schedule(startDateTime, duration, appointment);
            doctor.schedule(startDateTime, duration, appointment);
        } 
        catch (SchedulingConflict e) {
            return false;
        }

        // atualiza procedures e o paciente e médico
        this.dataManager.add(this.dataManager.getProceduresFile(), appointment);

        this.dataManager.update(
            this.dataManager.getUsersFile(), 
            patient, 
            p -> p.getEmail().equals(patient.getEmail())
        );

        this.dataManager.update(
            this.dataManager.getUsersFile(), 
            doctor, 
            d -> d.getEmail().equals(doctor.getEmail())
        );
        
        return true;
    }

    public boolean examinationCreated(String name, LocalDateTime startDateTime, Duration duration, Doctor doctor) {
        Patient patient = (Patient) this.user;
        Appointment examination = new Appointment(name, startDateTime, duration, patient, doctor);

        if (this.user == null || !(this.user instanceof Patient)) {
            System.out.println("Only patients can schedule examinations.");
            return false;
        }

        // precisa verificar se patient, doctor e rooms isAvailable antes de schedule, pq mesmo q tenha essa verificação no shedule,
        // pode ser que um médico esteja livre mas a sala não e salvaria o médico no json
        if (!patient.isAvailable(startDateTime, duration)) {
            System.out.println("You already have an appointment scheduled at that time");
        }
        if (!doctor.isAvailable(startDateTime, duration)) {
            System.out.println("Doctor" + doctor.getName() + "isn't available at that time.");
        }
        // if (!room.isAvailable(startDateTime, duration)) {
        //     System.out.println("The room isn't available");
        // }

        // assim pode ser um unico try-catch
        try {
            patient.schedule(startDateTime, duration, examination);
            doctor.schedule(startDateTime, duration, examination);
            // room.schedule(startDateTime, duration, examination);
        } 
        catch (SchedulingConflict e) {
            return false;
        }

        this.dataManager.add(this.dataManager.getProceduresFile(), examination);

        this.dataManager.update(
            this.dataManager.getUsersFile(), 
            patient, 
            p -> p.getEmail().equals(patient.getEmail())
        );

        this.dataManager.update(
            this.dataManager.getUsersFile(), 
            doctor, 
            d -> d.getEmail().equals(doctor.getEmail())
        );

        return true;
    }

}

