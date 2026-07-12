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
        return "You have successfully logged in.";
    }

    public String registrationSuccessful(String name, String email, String password, String role, DataManager dataManager) {
        try {
            this.user = authenticator.register(name, email, password, role, dataManager);
        }
        catch (UserAlreadyExists e) {
            return e.getMessage();
        }
        return "You have successfully completed your registration.";
    }   

    public String appointmentCreated(String name, LocalDateTime startDateTime, Duration duration, Doctor doctor) {
        try {
            if (this.user == null) {
                throw new UserNotLoggedIn("User not logged in."); 
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
            return "Only patients can schedule appointments.";
        }

        Appointment appointment = new Appointment(name, startDateTime, duration, patient, doctor);

        try {
            if (patient.isAvailable(startDateTime, duration))
                doctor.schedule(startDateTime, duration, appointment);
        }
        catch (SchedulingConflict e) {
            return e.getMessage();
        }

        try {
            patient.schedule(startDateTime, duration, appointment);
        }
        catch (SchedulingConflict e) {
            return e.getMessage();
        }

        this.dataManager.add(this.dataManager.getProceduresFile(), appointment);
        this.dataManager.update(this.dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        this.dataManager.update(this.dataManager.getUsersFile(), doctor, d -> d.getEmail().equals(doctor.getEmail()));
        
        return "You have successfully scheduled your appointment.";
    }

    public String examinationCreated(String name, LocalDateTime startDateTime, Duration duration, ExaminationRoom room) {
        try {
            if (this.user == null) {
                throw new UserNotLoggedIn("User not logged in."); 
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
            return "Only patients can schedule appointments.";
        }

        Examination examination = new Examination(name, startDateTime, duration, patient, room);

        try {
            if (patient.isAvailable(startDateTime, duration))
                room.schedule(startDateTime, duration, examination);
        }
        catch (SchedulingConflict e) {
            return e.getMessage();
        }

        try {
            patient.schedule(startDateTime, duration, examination);
        }
        catch (SchedulingConflict e) {
            return e.getMessage();
        }

        this.dataManager.add(this.dataManager.getProceduresFile(), examination);
        this.dataManager.update(this.dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        this.dataManager.update(this.dataManager.getUsersFile(), room, r -> r.getID().equals(room.getID()));
        
        return "You have successfully scheduled your examination.";
    }

    public String surgeryCreated() {return "";}

    public String appointmentCanceled(Appointment appointment) {
        try {
            if (this.user == null) {
                throw new UserNotLoggedIn("User not logged in."); 
            }
        } 
        catch (UserNotLoggedIn e) {
            return e.getMessage();
        }

        Patient patient = appointment.getPatient();
        try {
            patient.cancel(appointment);
        }
        catch (ProcedureDoesNotExist e) {
            return e.getMessage();
        }

        Doctor doctor = appointment.getDoctor();
        try {
            doctor.cancel(appointment);
        }
        catch (ProcedureDoesNotExist e) {
            return e.getMessage();
        }

        this.dataManager.delete(this.dataManager.getProceduresFile(), appointment);
        this.dataManager.update(this.dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        this.dataManager.update(this.dataManager.getUsersFile(), doctor, d -> d.getEmail().equals(doctor.getEmail()));

        return "You have successfully canceled your appointment.";
    }

    public String examinationCanceled(Examination examination) {
        try {
            if (this.user == null) {
                throw new UserNotLoggedIn("User not logged in."); 
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
            return "Only patients can cancel examinations.";
        }

        Patient patient = appointment.getPatient();
        try {
            patient.cancel(appointment);
        }
        catch (ProcedureDoesNotExist e) {
            return e.getMessage();
        }

        Doctor doctor = appointment.getDoctor();
        try {
            doctor.cancel(appointment);
        }
        catch (ProcedureDoesNotExist e) {
            return e.getMessage();
        }

        this.dataManager.delete(this.dataManager.getProceduresFile(), appointment);
        this.dataManager.update(this.dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        this.dataManager.update(this.dataManager.getUsersFile(), doctor, d -> d.getEmail().equals(doctor.getEmail()));

        return "You have successfully canceled your appointment.";
    }

    public boolean examinationCanceled(Examination examination) {
        return true;
    }

    public boolean surgeryCanceled(Surgery surgery) {
        return true;
    }

    /***
     * Algoritmo que agenda cirurgias de acordo com uma fila de prioridade
     * @return
     */
    public void surgeryScheduler(ArrayList<Surgery> priorityLine, ArrayList<SurgeryRoom> rooms) {}

}

