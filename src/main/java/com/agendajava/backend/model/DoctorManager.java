package com.agendajava.backend.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agendajava.backend.model.Manager.Specialty;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.User;

public class DoctorManager {
    private Map<Specialty, List<Doctor>> doctors = new HashMap<>();
    private Map<Specialty, List<Doctor>> surgeonDoctors = new HashMap<>();

    public DoctorManager(DataManager dataManager) {
        for (Specialty s : Specialty.values()) {
              doctors.put(s, new ArrayList<>());
              surgeonDoctors.put(s, new ArrayList<>());
        }

        List<User> users = dataManager.findAll(dataManager.getUsersFile(), User.class);
        if (users != null) {
            for (User u : users) {
                if (u instanceof Doctor) {
                    Doctor doc = (Doctor) u;
                    Specialty spec = doc.getSpecialty();
                    if (spec != null) {
                        doctors.get(spec).add(doc);
                        
                        if (doc.isSurgeon()) {
                            surgeonDoctors.get(spec).add(doc);
                        }
                    }
                }
            }
        }
    }

    public List<Doctor> getDoctorsOf(Specialty specialty) {
        return this.doctors.get(specialty);
    }

    public List<Doctor> getSurgeonsOf(Specialty specialty) {
        return this.surgeonDoctors.get(specialty);
    }

    public List<Doctor> getAnestesists() {
        return this.doctors.get(Specialty.ANESTESIOLOGIA);
    }

    public void addDoctorOf(Doctor newDoctor) {
        this.doctors.get(newDoctor.getSpecialty()).add(newDoctor);
        if (newDoctor.isSurgeon()) {
            addSurgeon(newDoctor);
        }
    }

    public void addSurgeon(Doctor newSurgeon) {
        this.surgeonDoctors.get(newSurgeon.getSpecialty()).add(newSurgeon);
    }
}