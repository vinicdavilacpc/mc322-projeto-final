package com.agendajava.backend.model;

import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.agendajava.backend.model.Manager.Specialty;
import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.SurgeryRoom;

/***
 * Classe que contém as filas de prioridade de cirurgia para cada especialidade
 * SurgeryManager
 */
public class SurgeryManager {
    private Map<Specialty, List<Surgery>> priorityLine = new HashMap<>();
    // Incluir blocos de horários!!!

    public SurgeryManager() {
        for (int i = 0; i < Specialty.values().size(); i++) 
            priorityLine.put(Specialty.values().get(i), new ArrayList<>());
    }

    public List<Surgery> getPLineOf(Specialty specialty) {
        return this.priorityLine.get(specialty);
    }

    /***
     * Algoritmo que agenda cirurgias de acordo com uma fila de prioridade
     * @return
     */
    public void surgeryScheduler(List<Specialty> specialties, ArrayList<Surgery> priorityLine, ArrayList<SurgeryRoom> rooms) {
        LocalDate dateNow = LocalDate.now();
        LocalDate startSchedule = dateNow.plusDays(1);

        for (int i = 0; i < specialties.size(); i++) {
            List<Surgery> surgeries = this.priorityLine.get(specialties.get(i));
            for (int j = 0; j < surgeries.size(); j++) {
                Surgery surgery = surgeries.get(j);

                

            }
        }
    }
}
