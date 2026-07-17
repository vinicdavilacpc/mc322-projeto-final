package com.agendajava.backend.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.agendajava.backend.exceptions.InvalidLogin;
import com.agendajava.backend.exceptions.ProcedureDoesNotExist;
import com.agendajava.backend.exceptions.SchedulingConflict;
import com.agendajava.backend.exceptions.UserAlreadyExists;
import com.agendajava.backend.exceptions.UserNotLoggedIn;
import com.agendajava.backend.exceptions.WrongUser;
import com.agendajava.backend.model.procedures.Appointment;
import com.agendajava.backend.model.procedures.Examination;
import com.agendajava.backend.model.procedures.Procedure;
import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.ExaminationRoom;
import com.agendajava.backend.model.rooms.Room;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import com.agendajava.backend.model.users.User;

public class Manager {
    private User user;
    private DataManager dataManager;
    private Authenticator authenticator;
    private SurgeryManager surgeryManager;
    private DoctorManager doctorManager;

    public enum Specialty {
        ANESTESIOLOGIA, CARDIOLOGIA, NEUROLOGIA, OFTALMOLOGIA, ORTOPEDIA;
    }

    public enum Priority {
        ELETIVA, URGENCIA, EMERGENCIA;
    }

    public Manager() {
        this.dataManager = new DataManager();
        this.authenticator = new Authenticator();
        this.surgeryManager = new SurgeryManager();
        this.doctorManager = new DoctorManager(dataManager);
        this.user = null;
    }

    private void populateUserCalendar(User u) {
        if (u == null) return;
        List<Procedure> procedures = dataManager.findAll(dataManager.getProceduresFile(), Procedure.class);
        if (procedures == null) return;
        
        for (Procedure p : procedures) {
            boolean isMine = false;
            if (p.getPatient() != null && p.getPatient().getEmail().equals(u.getEmail())) {
                isMine = true;
            } else if (p instanceof Appointment) {
                Appointment app = (Appointment) p;
                if (app.getDoctor() != null && app.getDoctor().getEmail().equals(u.getEmail())) {
                    isMine = true;
                }
            } else if (p instanceof Surgery) {
                Surgery surg = (Surgery) p;
                if (surg.getDoctor() != null && surg.getDoctor().getEmail().equals(u.getEmail())) {
                    isMine = true;
                }
            }
            
            if (isMine) {
                u.getCalendar().computeIfAbsent(p.getStarDateTime().toLocalDate(), k -> new TreeMap<>())
                 .put(p.getStarDateTime().toLocalTime(), p);
            }
        }
    }

    private void populateRoomCalendar(Room r) {
        if (r == null) return;
        List<Procedure> procedures = dataManager.findAll(dataManager.getProceduresFile(), Procedure.class);
        if (procedures == null) return;
        
        for (Procedure p : procedures) {
            if (p instanceof Examination) {
                Examination ex = (Examination) p;
                if (ex.getRoom() != null && ex.getRoom().getName().equals(r.getName())) {
                    r.getCalendar().computeIfAbsent(p.getStarDateTime().toLocalDate(), k -> new TreeMap<>())
                     .put(p.getStarDateTime().toLocalTime(), p);
                }
            }
        }
    }

    public Doctor getDoctorByEmail(String email) {
        User foundUser = dataManager.findOne(dataManager.getUsersFile(), User.class, u -> u.getEmail().equals(email));
        if (foundUser instanceof Doctor) {
            Doctor doc = (Doctor) foundUser;
            populateUserCalendar(doc);
            return doc;
        }
        return null;
    }

    public ExaminationRoom getExaminationRoomByName(String name) {
        Room foundRoom = dataManager.findOne(dataManager.getRoomsFile(), Room.class, r -> r.getName().equals(name));
        if (foundRoom instanceof ExaminationRoom) {
            ExaminationRoom room = (ExaminationRoom) foundRoom;
            populateRoomCalendar(room);
            return room;
        }
        return null;
    }

    public String loginSuccessful(String email, String password) {
        try {
            user = authenticator.login(email, password, dataManager);
            populateUserCalendar(user);
        } catch (InvalidLogin e) {
            return e.getMessage();
        }
        return "Login sucessful";
    }

    public String registrationSuccessful(String name, String email, String password, String role, Specialty specialty, DataManager dataManager) {
        try {
            user = authenticator.register(name, email, password, role, specialty, dataManager);
            populateUserCalendar(user);
        } catch (UserAlreadyExists e) {
            return e.getMessage();
        }
        return "Registration sucessful";
    }

    public String appointmentScheduled(String name, LocalDateTime startDateTime, Duration duration, Doctor doctor) {
        try {
            if (user == null) {
                throw new UserNotLoggedIn("Log in to schedule an appointment");
            }
        } catch (UserNotLoggedIn e) {
            return e.getMessage();
        }
        Patient patient;
        try {
            patient = (Patient) user;
        } catch (ClassCastException e) {
            return "Doctors cannot schedule appointments";
        }
        Appointment appointment = new Appointment(name, startDateTime, duration, patient, doctor);
        try {
            if (patient.isAvailable(startDateTime, duration))
                doctor.schedule(startDateTime, duration, appointment);
        } catch (SchedulingConflict e) {
            return doctor.getName() + " is unavailable at that time";
        }
        try {
            patient.schedule(startDateTime, duration, appointment);
        } catch (SchedulingConflict e) {
            return "You already have a procedure scheduled at that time";
        }
        dataManager.add(dataManager.getProceduresFile(), appointment);
        dataManager.update(dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        dataManager.update(dataManager.getUsersFile(), doctor, d -> d.getEmail().equals(doctor.getEmail()));
        return "Appointment scheduled";
    }

    public String examinationScheduled(String name, LocalDateTime startDateTime, Duration duration, ExaminationRoom room) {
        try {
            if (user == null) {
                throw new UserNotLoggedIn("Log in to schedule an examination");
            }
        } catch (UserNotLoggedIn e) {
            return e.getMessage();
        }
        Patient patient;
        try {
            patient = (Patient) user;
        } catch (ClassCastException e) {
            return "Doctors cannot schedule examinations";
        }
        Examination examination = new Examination(name, startDateTime, duration, patient, room);
        try {
            if (patient.isAvailable(startDateTime, duration))
                room.schedule(startDateTime, duration, examination);
        } catch (SchedulingConflict e) {
            return "The examination room is unavailable at that time";
        }
        try {
            patient.schedule(startDateTime, duration, examination);
        } catch (SchedulingConflict e) {
            return "You already have a procedure scheduled at that time";
        }
        dataManager.add(dataManager.getProceduresFile(), examination);
        dataManager.update(dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        dataManager.update(dataManager.getRoomsFile(), room, r -> r.getName().equals(room.getName()));
        return "Examination scheduled";
    }

    public String surgeryScheduled() {
        return "Em desenvolvimento";
    }

    public String appointmentCanceled(Appointment appointment) {
        try {
            if (user == null) {
                throw new UserNotLoggedIn("Log in to cancel an appointment");
            }
        } catch (UserNotLoggedIn e) {
            return e.getMessage();
        }
        Patient patient = appointment.getPatient();
        Doctor doctor = appointment.getDoctor();
        try {
            if (user != patient && user != doctor) {
                throw new WrongUser("You do not have permission to cancel this examination");
            }
        } catch (WrongUser e) {
            return e.getMessage();
        }
        try {
            patient.cancel(appointment);
        } catch (ProcedureDoesNotExist e) {
            return "This appointment could not be found";
        }
        try {
            doctor.cancel(appointment);
        } catch (ProcedureDoesNotExist e) {
            return "This appointment could not be found";
        }
        dataManager.delete(dataManager.getProceduresFile(), appointment);
        dataManager.update(dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        dataManager.update(dataManager.getUsersFile(), doctor, d -> d.getEmail().equals(doctor.getEmail()));
        return "Appointment canceled";
    }

    public String examinationCanceled(Examination examination) {
        try {
            if (user == null) {
                throw new UserNotLoggedIn("Log in to cancel an examination");
            }
        } catch (UserNotLoggedIn e) {
            return e.getMessage();
        }
        Patient patient;
        try {
            patient = (Patient) user;
        } catch (ClassCastException e) {
            return "Doctors cannot cancel examinations";
        }
        try {
            if (patient != examination.getPatient()) {
                throw new WrongUser("You do not have permission to cancel this examination");
            }
        } catch (WrongUser e) {
            return e.getMessage();
        }
        try {
            patient.cancel(examination);
        } catch (ProcedureDoesNotExist e) {
            return "This examination could not be found";
        }
        ExaminationRoom room = examination.getRoom();
        try {
            room.cancel(examination);
        } catch (ProcedureDoesNotExist e) {
            return "This examination could not be found";
        }
        dataManager.delete(dataManager.getProceduresFile(), examination);
        dataManager.update(dataManager.getUsersFile(), patient, p -> p.getEmail().equals(patient.getEmail()));
        dataManager.update(dataManager.getRoomsFile(), room, r -> r.getName().equals(room.getName()));
        return "Examination canceled";
    }

    public String surgeryCanceled(Surgery surgery) {
        return "Em desenvolvimento";
    }

    public List<String> getMeusProcedimentosFormatados() {
        List<String> result = new ArrayList<>();
        if (user == null) return result;
        
        List<Procedure> procedures = dataManager.findAll(dataManager.getProceduresFile(), Procedure.class);
        if (procedures == null) return result;
        
        for (Procedure p : procedures) {
            boolean isMine = false;
            if (p.getPatient() != null && p.getPatient().getEmail().equals(user.getEmail())) {
                isMine = true;
            } else if (p instanceof Appointment) {
                Appointment app = (Appointment) p;
                if (app.getDoctor() != null && app.getDoctor().getEmail().equals(user.getEmail())) {
                    isMine = true;
                }
            } else if (p instanceof Surgery) {
                Surgery surg = (Surgery) p;
                if (surg.getDoctor() != null && surg.getDoctor().getEmail().equals(user.getEmail())) {
                    isMine = true;
                }
            }
            
            if (isMine) {
                String detalhes = p.getName() + " | Data: " + p.getStarDateTime().toLocalDate() + 
                                  " às " + p.getStarDateTime().toLocalTime() + 
                                  " | Duração: " + p.getDuration().toMinutes() + "min";
                result.add(detalhes);
            }
        }
        return result;
    }
}