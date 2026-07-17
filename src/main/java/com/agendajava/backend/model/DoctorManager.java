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
    private Map<Specialty, List<Doctor>> anestesistDoctors = new HashMap<>();

    public DoctorManager(DataManager dataManager) {
        for (int i = 0; i < Specialty.values().length; i++) {
              doctors.put(Specialty.values()[i], new ArrayList<>());
              surgeonDoctors.put(Specialty.values()[i], new ArrayList<>());
              anestesistDoctors.put(Specialty.values()[i], new ArrayList<>());
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
                        } else {
                            anestesistDoctors.get(spec).add(doc);
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

    public List<Doctor> getAnestesistsOf(Specialty specialty) {
        return this.anestesistDoctors.get(specialty);
    }

    public void addDoctorOf(Doctor newDoctor) {
        this.doctors.get(newDoctor.getSpecialty()).add(newDoctor);
        if (newDoctor.isSurgeon())
            addSurgeon(newDoctor);
        else 
            anestesistDoctors.get(newDoctor.getSpecialty()).add(newDoctor);
    }

    public void addSurgeon(Doctor newSurgeon) {
        this.surgeonDoctors.get(newSurgeon.getSpecialty()).add(newSurgeon);
    }
}