package com.agendajava.backend.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agendajava.backend.model.Manager.Specialty;
import com.agendajava.backend.model.users.Doctor;

public class DoctorManager {
    private Map<Specialty, List<Doctor>> doctors = new HashMap<>();
    private Map<Specialty, List<Doctor>> surgeonDoctors = new HashMap<>();

    public DoctorManager(DataManager dataManager) {
        for (int i = 0; i < Specialty.values().length; i++) {
            doctors.put(Specialty.values()[i], new ArrayList<>()); 
            surgeonDoctors.put(Specialty.values()[i], new ArrayList<>());
        }
    }

    public List<Doctor> getDoctorsOf(Specialty specialty) {
        return this.doctors.get(specialty);
    }

    public List<Doctor> getSurgeonsOf(Specialty specialty) {
        return this.surgeonDoctors.get(specialty);
    }

    public void addDoctorOf(Doctor newDoctor) {
        this.doctors.get(newDoctor.getSpecialty()).add(newDoctor);
        if (newDoctor.isSurgeon())
            addSurgeon(newDoctor);
    }

    public void addSurgeon(Doctor newSurgeon) {
        this.doctors.get(newSurgeon.getSpecialty()).add(newSurgeon);
    }

}
