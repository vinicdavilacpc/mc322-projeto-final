package com.agendajava.backend.model;

import java.util.List;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agendajava.backend.exceptions.ImpossibleSurgery;
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

        for (Specialty value : Specialty.values()) {
            priorityLine.put(value, new ArrayList<>());
        }

        /** Os blocos de horários dedicados a cada especialidade são parâmetros da própria clinica e devem ser fornecidos previamente.
         * Neste sistema estamos considerando a divisão apresentada no README. A ideia do hashmap de blocos de horários é poder encontrar
         * mais facilmente os blocos por especialidade para realizar agendamentos
         */
        specialtyTimeBlocks.put(Specialty.CARDIOLOGIA, new HashMap<>());
        specialtyTimeBlocks.put(Specialty.NEUROLOGIA, new HashMap<>());
        specialtyTimeBlocks.put(Specialty.OFTALMOLOGIA, new HashMap<>());
        specialtyTimeBlocks.put(Specialty.ORTOPEDIA, new HashMap<>());
        // Obs: ANESTESIOLOGIA não está nessa estrutura de blocos de horários pois participa de todos!

        specialtyTimeBlocks.get(Specialty.CARDIOLOGIA).put(DayOfWeek.valueOf("TUESDAY"), new ArrayList<>(Arrays.asList(LocalTime.of(7, 0), LocalTime.of(12, 0))));
        specialtyTimeBlocks.get(Specialty.CARDIOLOGIA).put(DayOfWeek.valueOf("THURSDAY"), new ArrayList<>(Arrays.asList(LocalTime.of(7, 0), LocalTime.of(12, 0))));
        specialtyTimeBlocks.get(Specialty.NEUROLOGIA).put(DayOfWeek.valueOf("MONDAY"), new ArrayList<>(Arrays.asList(LocalTime.of(13, 0), LocalTime.of(18, 0))));
        specialtyTimeBlocks.get(Specialty.NEUROLOGIA).put(DayOfWeek.valueOf("WEDNESDAY"), new ArrayList<>(Arrays.asList(LocalTime.of(13, 0), LocalTime.of(18, 0))));
        specialtyTimeBlocks.get(Specialty.NEUROLOGIA).put(DayOfWeek.valueOf("FRIDAY"), new ArrayList<>(Arrays.asList(LocalTime.of(7, 0), LocalTime.of(12, 0))));
        specialtyTimeBlocks.get(Specialty.OFTALMOLOGIA).put(DayOfWeek.valueOf("TUESDAY"), new ArrayList<>(Arrays.asList(LocalTime.of(13, 0), LocalTime.of(18, 0))));
        specialtyTimeBlocks.get(Specialty.OFTALMOLOGIA).put(DayOfWeek.valueOf("THURSDAY"), new ArrayList<>(Arrays.asList(LocalTime.of(13, 0), LocalTime.of(18, 0))));
        specialtyTimeBlocks.get(Specialty.ORTOPEDIA).put(DayOfWeek.valueOf("MONDAY"), new ArrayList<>(Arrays.asList(LocalTime.of(7, 0), LocalTime.of(12, 0))));
        specialtyTimeBlocks.get(Specialty.ORTOPEDIA).put(DayOfWeek.valueOf("WEDNESDAY"), new ArrayList<>(Arrays.asList(LocalTime.of(7, 0), LocalTime.of(12, 0))));
        specialtyTimeBlocks.get(Specialty.ORTOPEDIA).put(DayOfWeek.valueOf("FRIDAY"), new ArrayList<>(Arrays.asList(LocalTime.of(13, 0), LocalTime.of(18, 0))));

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
    public String surgeryScheduler(ArrayList<Surgery> priorityLine, ArrayList<SurgeryRoom> rooms) {
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

                /* EMERGÊNCIA */
                if (surgery.isEmergency()) {
                    startDateTime = LocalDateTime.now(); // Emergências devem ser agendadas no dia atual!
                    LocalDateTime maxDateTime = LocalDateTime.of(startDateTime.toLocalDate(), LocalTime.of(18, 0)); // Horário de fechamento da clínica no dia
                    LocalDateTime maxStartDateTime = maxDateTime.minus(surgery.getDuration()); // Horário máximo que a cirurgia pode ser agendada

                    while (!couldSchedule(surgery, startDateTime, 0)) {
                        try {
                            if (startDateTime.plusHours(1).isAfter(maxStartDateTime)) {
                                throw new ImpossibleSurgery("Impossible to schedule " + surgery.getName() + " for patient " + surgery.getPatient().getName() + "!\n"); 
                            } else {
                                startDateTime = startDateTime.plusHours(1);
                            }
                        } catch (ImpossibleSurgery e) {
                            return e.getMessage();
                        }
                    }

                /* URGÊNCIA */
                } else if (surgery.isUrgency()) {
                    Map<DayOfWeek, List<LocalTime>> timeBlocks = specialtyTimeBlocks.get(currentSpecialty); // Blocos de horário possíveis para a especialidade
                    LocalDate startDate = LocalDate.now().plusDays(1);
                    DayOfWeek d = startDate.getDayOfWeek();

                    while (!timeBlocks.containsKey(d)) { // Garante um dia da semana válido para a especialidade
                        startDate = startDate.plusDays(1);
                        d = startDate.getDayOfWeek();
                    }

                    startDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(0)); // O horário de início é o do início do bloco
                    LocalDateTime maxDateTime = LocalDateTime.of(startDateTime.toLocalDate(), timeBlocks.get(d).get(1)); // Horário de fechamento da clínica no dia do bloco
                    LocalDateTime maxStartDateTime = maxDateTime.minus(surgery.getDuration()); // Horário máximo que a cirurgia pode ser agendada no dia do bloco

                    // Obs: cirurgias urgentes só serão impossíveis de agendar caso a próxima data possível seja depois do prazo limite da cirurgia
                    while (!couldSchedule(surgery, startDateTime, 0)) { 
                        try {
                            if (startDateTime.plusHours(1).isAfter(maxStartDateTime)) {
                                startDate = startDate.plusDays(1);
                                while (!timeBlocks.containsKey(d)) { // Garante um dia da semana válido para a especialidade
                                    startDate = startDate.plusDays(1);
                                    maxDateTime = LocalDateTime.of(startDateTime.toLocalDate(), timeBlocks.get(d).get(1));
                                    maxStartDateTime = maxDateTime.minus(surgery.getDuration());
                                }
                                if (startDate.isAfter(surgery.getLimitDate())) {
                                    throw new ImpossibleSurgery("Impossible to schedule " + surgery.getName() + " for patient " + surgery.getPatient().getName() + "!\n"); 
                                }
                                d = startDate.getDayOfWeek();
                                startDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(0));
                            } else {
                                startDateTime = startDateTime.plusHours(1);
                            }
                        } catch (ImpossibleSurgery e) {
                            return e.getMessage();
                        }
                    }

                /* ELETIVA */
                } else {
                    Map<DayOfWeek, List<LocalTime>> timeBlocks = specialtyTimeBlocks.get(currentSpecialty); // Blocos de horário possíveis para a especialidade
                    LocalDate startDate = LocalDate.now().plusDays(1);
                    DayOfWeek d = startDate.getDayOfWeek();

                    while (!timeBlocks.containsKey(d)) { // Garante um dia da semana válido para a especialidade
                        startDate = startDate.plusDays(1);
                        d = startDate.getDayOfWeek();
                    }

                    startDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(0)); // O horário de início é o do início do bloco
                    LocalDateTime maxDateTime = LocalDateTime.of(startDateTime.toLocalDate(), timeBlocks.get(d).get(1)); // Horário de fechamento da clínica no dia do bloco
                    LocalDateTime maxStartDateTime = maxDateTime.minus(surgery.getDuration()); // Horário máximo que a cirurgia pode ser agendada no dia do bloco

                    // Obs: cirurgias eletivas nunca serão impossíveis de agendar
                    while (!couldSchedule(surgery, startDateTime, 1)) {
                        if (startDateTime.plusHours(1).isAfter(maxStartDateTime)) {
                            startDate = startDate.plusDays(1);
                            while (!timeBlocks.containsKey(d)) { // Garante um dia da semana válido para a especialidade
                                startDate = startDate.plusDays(1);
                                maxDateTime = LocalDateTime.of(startDateTime.toLocalDate(), timeBlocks.get(d).get(1));
                                maxStartDateTime = maxDateTime.minus(surgery.getDuration());
                            }
                            d = startDate.getDayOfWeek();
                            startDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(0));
                        } else {
                            startDateTime = startDateTime.plusHours(1);
                        }
                }
            }
        }
    }

    /***
     * Método que agenda uma cirurgia quando possível e retorna se foi possível ou não
     */
    private boolean couldSchedule(Surgery surgery, LocalDateTime startDateTime, int startRoom) {
        /* #3 - Seleciona o anestesista */
        Specialty currentSpecialty = surgery.getSpecialty();
        List<Doctor> anestesists = doctorManager.getAnestesistsOf(currentSpecialty);
        boolean scheduled = false;

        for (int a = 0; a < anestesists.size() && !scheduled; a++) {
            if (anestesists.get(a).isAvailable(startDateTime, surgery.getDuration())) {

                /* #4 - Seleciona o cirurgião */
                List<Doctor> surgeons = doctorManager.getSurgeonsOf(currentSpecialty);
                for (int b = 0; b < surgeons.size() && !scheduled; b++) {
                    if (surgeons.get(b).isAvailable(startDateTime, surgery.getDuration())) {

                        /* #5 - Seleciona a sala */
                        for (int c = startRoom; c < surgeryRooms.size() && !scheduled; c++) { // Obs: cirurgias eletivas nunca são agendadas na SC00
                            if (surgeryRooms.get(c).isAvailable(startDateTime, surgery.getDuration())) {

                                /* #6 - Seleciona um leito na RPA, caso necessário */
                                if (surgery.needsICU()) {
                                    if (icuRoom.hasBedsAvailable(startDateTime, surgery.getDuration())) {
                                        icuRoom.bedSchedule(startDateTime, surgery.getDuration(), surgery);
                                        anestesists.get(a).schedule(startDateTime, surgery.getDuration(), surgery);
                                        surgeons.get(b).schedule(startDateTime, surgery.getDuration(), surgery);
                                        surgeryRooms.get(c).schedule(startDateTime, surgery.getDuration(), surgery);
                                        return true;
                                    } else {
                                        return false;
                                    }
                                } else {
                                    anestesists.get(a).schedule(startDateTime, surgery.getDuration(), surgery);
                                    surgeons.get(b).schedule(startDateTime, surgery.getDuration(), surgery);
                                    surgeryRooms.get(c).schedule(startDateTime, surgery.getDuration(), surgery);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
