package com.agendajava.backend.model;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.agendajava.backend.model.Manager.Specialty;
import com.agendajava.backend.model.procedures.Surgery;

/***
 * Classe que contém as filas de prioridade de cirurgia para cada especialidade
 * SurgeryManager
 */
public class SurgeryManager {
    private Map<Specialty, List<Surgery>> line = new HashMap<>();
    
    public SurgeryManager(List<Specialty> specialties) {
        for (int i = 0; i < specialties.size(); i++) 
            line.put(specialties.get(i), new ArrayList<>());
    }

    
}
