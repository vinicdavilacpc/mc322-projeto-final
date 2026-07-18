package com.agendajava.backend.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

public class SurgeryManager {
    private Map<Specialty, List<Surgery>> priorityLine = new HashMap<>();
    private Map<Specialty, Map<DayOfWeek, List<LocalTime>>> specialtyTimeBlocks = new HashMap<>(); 
    private List<SurgeryRoom> surgeryRooms;
    private ICURoom icuRoom;
    private DoctorManager doctorManager;
    private DataManager dataManager;

    public SurgeryManager(int nSurgeryRooms, int nBedsICU, DoctorManager doctorManager, DataManager dataManager) {
        this.doctorManager = doctorManager;
        this.dataManager = dataManager; 
        
        for (Specialty value : Specialty.values()) {
            priorityLine.put(value, new ArrayList<>());
        }

        specialtyTimeBlocks.put(Specialty.CARDIOLOGIA, new HashMap<>());
        specialtyTimeBlocks.put(Specialty.NEUROLOGIA, new HashMap<>());
        specialtyTimeBlocks.put(Specialty.OFTALMOLOGIA, new HashMap<>());
        specialtyTimeBlocks.put(Specialty.ORTOPEDIA, new HashMap<>());
        
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
        List<Surgery> pLine = priorityLine.get(surgery.getSpecialty()); 
        
        if (pLine.isEmpty()) { 
            pLine.add(surgery);
        } else {
            int i;
            for (i = (pLine.size() - 1); i >= 0; i--) {
                if (surgery.getClinicalPriority() <= pLine.get(i).getClinicalPriority()) { 
                    pLine.add((i + 1), surgery);
                    return;
                }
            }
            pLine.add(0, surgery);
        }
    }

    public String surgeryScheduler(ArrayList<Surgery> priorityLine, ArrayList<SurgeryRoom> rooms) {
        LocalDateTime startDateTime;
        LocalDate startDate; 
        DayOfWeek d; 
        
        for (Specialty currentSpecialty : Specialty.values()) {
            List<Surgery> surgeries = this.priorityLine.get(currentSpecialty);
            
            List<Doctor> anestesists = doctorManager.getAnestesists();
            List<Doctor> surgeons = doctorManager.getSurgeonsOf(currentSpecialty);
            
            if (!surgeries.isEmpty() && (anestesists == null || anestesists.isEmpty() || surgeons == null || surgeons.isEmpty())) {
                return "Erro: Faltam Cirurgiões de " + currentSpecialty + " ou Anestesistas cadastrados no sistema!";
            }

            for (int j = 0; j < surgeries.size(); j++) {
                Surgery surgery = surgeries.get(j); 
                
                if (surgery.isEmergency()) {
                    startDateTime = LocalDateTime.now(); 
                    LocalDateTime maxDateTime = LocalDateTime.of(startDateTime.toLocalDate(), LocalTime.of(18, 0)); 
                    LocalDateTime maxStartDateTime = maxDateTime.minus(surgery.getDuration()); 
                    
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
                } else if (surgery.isUrgency()) {
                    Map<DayOfWeek, List<LocalTime>> timeBlocks = specialtyTimeBlocks.get(currentSpecialty); 
                    
                    if (timeBlocks == null) continue; 
                    
                    startDate = LocalDate.now().plusDays(1);
                    d = startDate.getDayOfWeek();
                    while (!timeBlocks.containsKey(d)) { 
                        startDate = startDate.plusDays(1);
                        d = startDate.getDayOfWeek();
                    }
                    startDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(0)); 
                    LocalDateTime maxDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(1)); 
                    LocalDateTime maxStartDateTime = maxDateTime.minus(surgery.getDuration()); 
                    
                    while (!couldSchedule(surgery, startDateTime, 0)) { 
                        try {
                            if (startDateTime.plusHours(1).isAfter(maxStartDateTime)) {
                                do {
                                    startDate = startDate.plusDays(1);
                                    d = startDate.getDayOfWeek();
                                } while (!timeBlocks.containsKey(d));
                                
                                if (surgery.getLimitDate() != null && startDate.isAfter(surgery.getLimitDate())) {
                                    throw new ImpossibleSurgery("Impossible to schedule " + surgery.getName() + " for patient " + surgery.getPatient().getName() + "!\n"); 
                                }
                                startDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(0));
                                maxDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(1));
                                maxStartDateTime = maxDateTime.minus(surgery.getDuration());
                            } else {
                                startDateTime = startDateTime.plusHours(1);
                            }
                        } catch (ImpossibleSurgery e) {
                            return e.getMessage();
                        }
                    }
                } else {
                    Map<DayOfWeek, List<LocalTime>> timeBlocks = specialtyTimeBlocks.get(currentSpecialty); 
                    
                    if (timeBlocks == null) continue; 
                    
                    startDate = LocalDate.now().plusDays(1);
                    d = startDate.getDayOfWeek();
                    while (!timeBlocks.containsKey(d)) { 
                        startDate = startDate.plusDays(1);
                        d = startDate.getDayOfWeek();
                    }
                    startDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(0)); 
                    LocalDateTime maxDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(1)); 
                    LocalDateTime maxStartDateTime = maxDateTime.minus(surgery.getDuration()); 
                    
                    while (!couldSchedule(surgery, startDateTime, 1)) {
                        try {
                            if (startDateTime.plusHours(1).isAfter(maxStartDateTime)) {
                                do {
                                    startDate = startDate.plusDays(1);
                                    d = startDate.getDayOfWeek();
                                } while (!timeBlocks.containsKey(d));
                                
                                if (surgery.getLimitDate() != null && startDate.isAfter(surgery.getLimitDate())) {
                                    throw new ImpossibleSurgery("Impossible to schedule " + surgery.getName() + " for patient " + surgery.getPatient().getName() + "!\n"); 
                                }

                                startDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(0));
                                maxDateTime = LocalDateTime.of(startDate, timeBlocks.get(d).get(1));
                                maxStartDateTime = maxDateTime.minus(surgery.getDuration());
                            } else {
                                startDateTime = startDateTime.plusHours(1);
                            }
                        } catch (ImpossibleSurgery e) {
                            return e.getMessage();
                        }
                    }
                } 
            } 
        } 
        
        for (Specialty currentSpecialty : Specialty.values()) {
            this.priorityLine.get(currentSpecialty).clear();
        }
        
        return "Fila de Cirurgias processada com sucesso! Verifique em Meus Agendamentos."; 
    }

    private boolean couldSchedule(Surgery surgery, LocalDateTime startDateTime, int startRoom) {
        Specialty currentSpecialty = surgery.getSpecialty(); 
        boolean scheduled = false; 
        
        List<Doctor> anestesists = doctorManager.getAnestesists();
        for (int a = 0; a < anestesists.size() && !scheduled; a++) {
            if (anestesists.get(a).isAvailable(startDateTime, surgery.getDuration())) {
                
                List<Doctor> surgeons = doctorManager.getSurgeonsOf(currentSpecialty);
                for (int b = 0; b < surgeons.size() && !scheduled; b++) {
                    if (surgeons.get(b).isAvailable(startDateTime, surgery.getDuration())) {
                        
                        for (int c = startRoom; c < surgeryRooms.size() && !scheduled; c++) { 
                            if (surgeryRooms.get(c).isAvailable(startDateTime, surgery.getDuration())) {
                                
                                if (surgery.needsICU()) {
                                    if (icuRoom.hasBedsAvailable(startDateTime, surgery.getDuration())) {
                                        icuRoom.bedSchedule(startDateTime, surgery.getDuration(), surgery);
                                        anestesists.get(a).schedule(startDateTime, surgery.getDuration(), surgery);
                                        surgeons.get(b).schedule(startDateTime, surgery.getDuration(), surgery);
                                        surgeryRooms.get(c).schedule(startDateTime, surgery.getDuration(), surgery);
                                        
                                        surgery.setSurgeon(surgeons.get(b));
                                        surgery.setRoom(surgeryRooms.get(c));
                                        surgery.setStart(startDateTime);
                                        dataManager.add(dataManager.getProceduresFile(), surgery);
                                        
                                        return true;
                                    } else {
                                        return false;
                                    }
                                } else {
                                    anestesists.get(a).schedule(startDateTime, surgery.getDuration(), surgery);
                                    surgeons.get(b).schedule(startDateTime, surgery.getDuration(), surgery);
                                    surgeryRooms.get(c).schedule(startDateTime, surgery.getDuration(), surgery);
                                    
                                    surgery.setSurgeon(surgeons.get(b));
                                    surgery.setRoom(surgeryRooms.get(c));
                                    surgery.setStart(startDateTime);
                                    dataManager.add(dataManager.getProceduresFile(), surgery);
                                    
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