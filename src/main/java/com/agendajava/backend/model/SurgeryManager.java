package com.agendajava.backend.model;

import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
    private Map<Specialty, List<Surgery>> priorityLine = new HashMap<>();          // Filas de prioridade das cirurgias por especialidade
    private Map<Specialty, List<TimeBlock>> specialtyTimeBlocks = new HashMap<>(); // Blocos de horários de cada especialidade
    private List<SurgeryRoom> surgeryRooms;
    private ICURoom icuRoom;


    public SurgeryManager(int nSurgeryRooms, int nBedsICU) {
        for (int i = 0; i < Specialty.values().length; i++) 
            priorityLine.put(Specialty.values()[i], new ArrayList<>());

        /** Os blocos de horários dedicados a cada especialidade são parâmetros da própria clinica e devem ser fornecidos previamente.
         * Neste sistema estamos considerando a divisão apresentada no README. A ideia do hashmap de blocos de horários é poder encontrar
         * mais facilmente os blocos por especialidade para realizar agendamentos
         */
        specialtyTimeBlocks.put(Specialty.CARDIOLOGIA, new ArrayList<>());
        specialtyTimeBlocks.put(Specialty.NEUROLOGIA, new ArrayList<>());
        specialtyTimeBlocks.put(Specialty.OFTALMOLOGIA, new ArrayList<>());
        specialtyTimeBlocks.put(Specialty.ORTOPEDIA, new ArrayList<>());
        // Obs: ANESTESIOLOGIA não está nessa estrutura de blocos de horários pois participa de todos!

        specialtyTimeBlocks.get(Specialty.CARDIOLOGIA).add(new TimeBlock(DayOfWeek.valueOf("TUESDAY"), LocalTime.of(7, 0), LocalTime.of(12, 0)));
        specialtyTimeBlocks.get(Specialty.CARDIOLOGIA).add(new TimeBlock(DayOfWeek.valueOf("THURSDAY"), LocalTime.of(7, 0), LocalTime.of(12, 0)));
        specialtyTimeBlocks.get(Specialty.NEUROLOGIA).add(new TimeBlock(DayOfWeek.valueOf("MONDAY"), LocalTime.of(13, 0), LocalTime.of(18, 0)));
        specialtyTimeBlocks.get(Specialty.NEUROLOGIA).add(new TimeBlock(DayOfWeek.valueOf("WEDNESDAY"), LocalTime.of(13, 0), LocalTime.of(18, 0)));
        specialtyTimeBlocks.get(Specialty.NEUROLOGIA).add(new TimeBlock(DayOfWeek.valueOf("FRIDAY"), LocalTime.of(7, 0), LocalTime.of(12, 0)));
        specialtyTimeBlocks.get(Specialty.OFTALMOLOGIA).add(new TimeBlock(DayOfWeek.valueOf("TUESDAY"), LocalTime.of(13, 0), LocalTime.of(18, 0)));
        specialtyTimeBlocks.get(Specialty.OFTALMOLOGIA).add(new TimeBlock(DayOfWeek.valueOf("THURSDAY"), LocalTime.of(13, 0), LocalTime.of(18, 0)));
        specialtyTimeBlocks.get(Specialty.ORTOPEDIA).add(new TimeBlock(DayOfWeek.valueOf("MONDAY"), LocalTime.of(7, 0), LocalTime.of(12, 0)));
        specialtyTimeBlocks.get(Specialty.ORTOPEDIA).add(new TimeBlock(DayOfWeek.valueOf("WEDNESDAY"), LocalTime.of(7, 0), LocalTime.of(12, 0)));
        specialtyTimeBlocks.get(Specialty.ORTOPEDIA).add(new TimeBlock(DayOfWeek.valueOf("FRIDAY"), LocalTime.of(13, 0), LocalTime.of(18, 0)));

        this.surgeryRooms = new ArrayList<SurgeryRoom>();
        for (int i = 0; i < nSurgeryRooms; i++) {
            surgeryRooms.add(new SurgeryRoom("SC0" + i));
        }

        this.icuRoom = new ICURoom("SALA RPA", nBedsICU); 


        
    }

    public List<Surgery> getPLineOf(Specialty specialty) {
        return this.priorityLine.get(specialty);
    }

    public void addToPriorityLine(Surgery surgery) {
        pLine = priorityLine.get(surgery.getSpecialty()); // Fila de prioridade da especialidade
        // Prioridade: Emergência > Urgência > Eletiva

        if (pLine.size() == 0) { // Fila vazia
            pLine.add(surgery);
        } else {
            int i;
            for (i = (pLine.size() - 1); i >= 0; i--) {
                if (surgery.getClinicalPriority() <= pLine.get(i).getClinicalPriority()) { // Existe uma prioridade maior à frente
                    pLine.add((i + 1), surgery);
                    return;
                }
            }
            pLine.add(i, surgery); // A nova cirurgia tem a prioridade máxima
        }
    }

    /***
     * Algoritmo que agenda cirurgias de acordo com uma fila de prioridade
     * @return
     */
    public void surgeryScheduler(ArrayList<Surgery> priorityLine, ArrayList<SurgeryRoom> rooms) {
        LocalDate dateNow = LocalDate.now();
        LocalDate startSchedule = dateNow.plusDays(1); // O scheduler inicia a agenda de agendamento sempre no dia seguinte ao atual

        for (int i = 0; i < Specialty.values().length; i++) {
            List<Surgery> surgeries = this.priorityLine.get(Specialty.values()[i]);
            for (int j = 0; j < surgeries.size(); j++) {
                Surgery surgery = surgeries.get(j); 
            }
        }
    }
}
