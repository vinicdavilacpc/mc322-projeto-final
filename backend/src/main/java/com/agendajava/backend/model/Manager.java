package com.agendajava.backend.model;

import java.time.LocalDateTime;
import java.time.Duration;
import com.agendajava.backend.exceptions.InvalidLogin;
import com.agendajava.backend.exceptions.ProcedureDoesNotExist;
import com.agendajava.backend.exceptions.SchedulingConflict;
import com.agendajava.backend.exceptions.UserAlreadyExists;
import com.agendajava.backend.exceptions.UserNotLoggedIn;
import com.agendajava.backend.model.procedures.Appointment;
import com.agendajava.backend.model.procedures.Examination;
import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.ExaminationRoom;
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

    public String loginSuccessful(String email, String password) {
        try {
            this.user = authenticator.login(email, password, dataManager);
        } 
        catch (InvalidLogin e) {
            return e.getMessage();
        }
        return "Login sucessful";
    }

    public String registrationSuccessful(String name, String email, String password, String role, String specialty, DataManager dataManager) {
        try {
            this.user = authenticator.register(name, email, password, role, specialty, dataManager);
        }
        catch (UserAlreadyExists e) {
            return e.getMessage();
        }
        return "Registration sucessful";
    }   

    public String appointmentScheduled(String name, LocalDateTime startDateTime, Duration duration, Doctor doctor) {
        try {
            if (this.user == null) {
                throw new UserNotLoggedIn("Log in to schedule an appointment"); 
            }
        } 
        catch (UserNotLoggedIn e) {
            return e.getMessage();
        }
        
        Patient patient;
        try {
            patient = (Patient) this.user;
        }
        catch (ClassCastException e) {
            return "Doctors cannot schedule appointments";
        }

        Appointment appointment = new Appointment(name, startDateTime, duration, patient, doctor);

        try {
            if (patient.isAvailable(startDateTime, duration))
                doctor.schedule(startDateTime, duration, appointment);
        }
        catch (SchedulingConflict e) {
            return doctor.getName() + " is unavailable at that time";
        }

        try {
            patient.schedule(startDateTime, duration, appointment);
        }
        catch (SchedulingConflict e) {
            return "You already have a procedure scheduled at that time";
        }

        this.dataManager.add(this.dataManager.getProceduresFile(), appointment);
        this.dataManager.update(this.dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        this.dataManager.update(this.dataManager.getUsersFile(), doctor, d -> d.getEmail().equals(doctor.getEmail()));
        
        return "Appointment scheduled";
    }

    public String examinationScheduled(String name, LocalDateTime startDateTime, Duration duration, ExaminationRoom room) {
        try {
            if (this.user == null) {
                throw new UserNotLoggedIn("Log in to schedule an examination"); 
            }
        } 
        catch (UserNotLoggedIn e) {
            return e.getMessage();
        }
        
        Patient patient;
        try {
            patient = (Patient) this.user;
        }
        catch (ClassCastException e) {
            return "Doctors cannot schedule examinations";
        }

        Examination examination = new Examination(name, startDateTime, duration, patient, room);

        try {
            if (patient.isAvailable(startDateTime, duration))
                room.schedule(startDateTime, duration, examination);
        }
        catch (SchedulingConflict e) {
            return "The examination room is unavailable at that time";
        }

        try {
            patient.schedule(startDateTime, duration, examination);
        }
        catch (SchedulingConflict e) {
            return "You already have a procedure scheduled at that time";
        }

        this.dataManager.add(this.dataManager.getProceduresFile(), examination);
        this.dataManager.update(this.dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        this.dataManager.update(this.dataManager.getRoomsFile(), room, r -> r.getID().equals(room.getID()));
        
        return "Examination scheduled";
    }

    public String surgeryScheduled();

    public String appointmentCanceled(Appointment appointment) {
        try {
            if (this.user == null) {
                throw new UserNotLoggedIn("Log in to cancel an appointment"); 
            }
        } 
        catch (UserNotLoggedIn e) {
            return e.getMessage();
        }

        Patient patient = appointment.getPatient();
        Doctor doctor = appointment.getDoctor();
        try {
            if (this.user != patient && this.user != doctor) {
                throw new WrongUser("You do not have permission to cancel this examination");
            }
        }
        catch (WrongUser e) {
            return e.getMessage();
        }

        try {
            patient.cancel(appointment);
        }
        catch (ProcedureDoesNotExist e) {
            return "This appointment could not be found";
        }

        try {
            doctor.cancel(appointment);
        }
        catch (ProcedureDoesNotExist e) {
            return "This appointment could not be found";
        }

        this.dataManager.delete(this.dataManager.getProceduresFile(), appointment);
        this.dataManager.update(this.dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        this.dataManager.update(this.dataManager.getUsersFile(), doctor, d -> d.getEmail().equals(doctor.getEmail()));

        return "Appointment canceled";
    }

    public String examinationCanceled(Examination examination) {
        try {
            if (this.user == null) {
                throw new UserNotLoggedIn("Log in to cancel an examination"); 
            }
        } 
        catch (UserNotLoggedIn e) {
            return e.getMessage();
        }

        Patient patient;
        try {
            patient = (Patient) this.user;
        }
        catch (ClassCastException e) {
            return "Doctors cannot cancel examinations";
        }

        try {
            if (patient != examination.getPatient()) {
                throw new WrongUser("You do not have permission to cancel this examination");
            }
        }
        catch (WrongUser e) {
            return e.getMessage();
        }

        try {
            patient.cancel(examination);
        }
        catch (ProcedureDoesNotExist e) {
            return "This examination could not be found";
        }

        ExaminationRoom room = examination.getRoom();
        try {
            room.cancel(examination);
        }
        catch (ProcedureDoesNotExist e) {
            return "This examination could not be found";
        }

        this.dataManager.delete(this.dataManager.getProceduresFile(), examination);
        this.dataManager.update(this.dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        this.dataManager.update(this.dataManager.getRoomsFile(), room, r -> r.getID().equals(room.getID()));

        return "Examination canceled";
    }

    public String surgeryCanceled(Surgery surgery);

    /***
     * Algoritmo que agenda cirurgias de acordo com uma fila de prioridade
     * @return
     */
    public void surgeryScheduler(ArrayList<Surgery> priorityLine, ArrayList<SurgeryRoom> rooms) {}

}

