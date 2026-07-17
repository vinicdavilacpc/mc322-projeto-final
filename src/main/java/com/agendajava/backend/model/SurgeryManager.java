package com.agendajava.backend.model;

import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.agendajava.backend.model.Manager.Specialty;
import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.ICURoom;
import com.agendajava.backend.model.rooms.SurgeryRoom;
import com.agendajava.backend.model.users.Doctor;

/***
 * Classe que contém as filas de prioridade de cirurgia para cada especialidade
 * SurgeryManager
 */
public class SurgeryManager {
    private Map<Specialty, List<Surgery>> priorityLine = new HashMap<>();          // Filas de prioridade das cirurgias por especialidade
    private Map<Specialty, Map<DayOfWeek, List<LocalTime>>> specialtyTimeBlocks = new HashMap<>(); // Blocos de horários de cada especialidade
    private List<SurgeryRoom> surgeryRooms;
    private ICURoom icuRoom;
    private DoctorManager doctorManager;


    public SurgeryManager(int nSurgeryRooms, int nBedsICU, DoctorManager doctorManager) {
        this.doctorManager = doctorManager;

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
        List<Surgery> pLine = priorityLine.get(surgery.getSpecialty()); // Fila de prioridade da especialidade
        // Prioridade: Emergência > Urgência > Eletiva

        if (pLine.isEmpty()) { // Fila vazia
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
        List<Surgery> reschedule = new ArrayList<Surgery>(); // Cirurgias que precisarão ser realocadas
        LocalDateTime startDateTime;
        LocalDate date; // Data para marcar a cirurgia
        LocalTime time; // Horário para marcar a cirurgia

        /* #1 - Percorre a fila de prioridade de cada especialidade */
        for (Specialty currentSpecialty : Specialty.values()) {
            List<Surgery> surgeries = this.priorityLine.get(currentSpecialty);
            
            for (int j = 0; j < surgeries.size(); j++) {
                Surgery surgery = surgeries.get(j); // Cirurgia da fila para agendar

                /* #2 Analisa de acordo com a prioridade */
                if (surgery.isEmergency()) {
                    startDateTime = LocalDateTime.now(); // Emergências devem ser agendadas no dia atual!

                    /* #3 - Seleciona o anestesista */
                    List<Doctor> anestesists = doctorManager.getAnestesistsOf(currentSpecialty);
                    for (int a = 0; a < anestesists.size(); a++) {
                        if (anestesists.get(a).isAvailable(startDateTime, surgery.getDuration())) {
                            /* #4 - Seleciona o cirurgião */
                            List<Doctor> surgeons = doctorManager.getSurgeonsOf(currentSpecialty);
                            for (int b = 0; b < surgeons.size(); b++) {
                                if (surgeons.get(b).isAvailable(startDateTime, surgery.getDuration())) {
                                    /* #5 - Seleciona a sala */
                                    for (int c = 0; c < surgeryRooms.size(); c++) {
                                        if (surgeryRooms.get(c).isAvailable(startDateTime, surgery.getDuration())) {
                                            /* #6 - Seleciona um leito na RPA, caso necessário */
                                            if (surgery.needsICU()) {
                                                if (icuRoom.hasBedsAvailable(startDateTime, surgery.getDuration())) {
                                                    icuRoom.bedSchedule(startDateTime, surgery.getDuration(), surgery);
                                                    anestesists.get(a).schedule(startDateTime, surgery.getDuration(), surgery);
                                                    surgeons.get(b).schedule(startDateTime, surgery.getDuration(), surgery);
                                                    surgeryRooms.get(c).schedule(startDateTime, surgery.getDuration(), surgery);
                                                } else {
                                                    // Mudar data (para o caso da RPA indisponível)
                                                }
                                            } else {
                                                anestesists.get(a).schedule(startDateTime, surgery.getDuration(), surgery);
                                                surgeons.get(b).schedule(startDateTime, surgery.getDuration(), surgery);
                                                surgeryRooms.get(c).schedule(startDateTime, surgery.getDuration(), surgery);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                } else if (surgery.isUrgency()) {
                    // FAZER
                } else {
                    boolean scheduled = false;
                    Map<DayOfWeek, List<LocalTime>> timeBlocks = specialtyTimeBlocks.get(currentSpecialty);
                    startDateTime = LocalDateTime.now().plusDays(1);
                    DayOfWeek d = startDateTieme.getDayOfWeek();

                    while (!timeBlocks.containsKey(d)) {
                        startDateTime = startDateTime.plusDays(1);
                        DayOfWeek d = startDateTieme.getDayOfWeek();
                    }

                    while (!scheduled) {
                        List<Doctor> anestesists = doctorManager.getAnestesistsOf(currentSpecialty);
                        for (int a = 0; a < anestesists.size(); a++) {
                            if (anestesists.get(a).isAvailable(startDateTime, surgery.getDuration())) {
                                /* #4 - Seleciona o cirurgião */
                                List<Doctor> surgeons = doctorManager.getSurgeonsOf(currentSpecialty);
                                for (int b = 0; b < surgeons.size(); b++) {
                                    if (surgeons.get(b).isAvailable(startDateTime, surgery.getDuration())) {
                                        /* #5 - Seleciona a sala */
                                        for (int c = 0; c < surgeryRooms.size(); c++) {
                                            if (surgeryRooms.get(c).isAvailable(startDateTime, surgery.getDuration())) {
                                                /* #6 - Seleciona um leito na RPA, caso necessário */
                                                if (surgery.needsICU()) {
                                                    if (icuRoom.hasBedsAvailable(startDateTime, surgery.getDuration())) {
                                                        icuRoom.bedSchedule(startDateTime, surgery.getDuration(), surgery);
                                                        anestesists.get(a).schedule(startDateTime, surgery.getDuration(), surgery);
                                                        surgeons.get(b).schedule(startDateTime, surgery.getDuration(), surgery);
                                                        surgeryRooms.get(c).schedule(startDateTime, surgery.getDuration(), surgery);
                                                    } else {
                                                        // Mudar data (para o caso da RPA indisponível)
                                                    }
                                                } else {
                                                    anestesists.get(a).schedule(startDateTime, surgery.getDuration(), surgery);
                                                    surgeons.get(b).schedule(startDateTime, surgery.getDuration(), surgery);
                                                    surgeryRooms.get(c).schedule(startDateTime, surgery.getDuration(), surgery);
                                                }
                                            }
                                        }
                                    }
                    }
                }
            }
        }
    }
}
