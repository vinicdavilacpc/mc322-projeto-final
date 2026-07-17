package com.agendajava.backend.model.rooms;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.TreeMap;

import com.agendajava.backend.exceptions.SchedulingConflict;
import com.agendajava.backend.interfaces.Schedulable;
import com.agendajava.backend.model.Equipment;
import com.agendajava.backend.model.procedures.Procedure;

public class SurgeryRoom extends Room implements Schedulable {
    
    public SurgeryRoom(String name, List<Equipment> equipments) {
        super(name, equipments);
    }

    public boolean isAvailable(LocalDateTime startDateTime, Duration duration) {
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        this.getCalendar().computeIfAbsent(date, d -> new TreeMap<>());
        TreeMap<LocalTime, Procedure> daymap = this.getCalendar().get(date);
        
        if (daymap.isEmpty()) return true;
        
        LocalTime priorProcedureStartTime = daymap.floorKey(startTime);
        if (priorProcedureStartTime != null && daymap.get(priorProcedureStartTime).overlapsWith(startDateTime, duration))
            return false;
            
        LocalTime nextProcedureStartTime = daymap.ceilingKey(startTime);
        if (nextProcedureStartTime != null && daymap.get(nextProcedureStartTime).overlapsWith(startDateTime, duration))
            return false;
            
        return true;
    }

    public void schedule(LocalDateTime startDateTime, Duration duration, Procedure procedure) {
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        this.getCalendar().computeIfAbsent(date, d -> new TreeMap<>());
        TreeMap<LocalTime, Procedure> daymap = this.getCalendar().get(date);
        
        if (!isAvailable(startDateTime, duration)) {
            throw new SchedulingConflict("Horário indisponível!");
        } 
        daymap.put(startTime, procedure);
    }

    @Override
    public void cancel(Procedure procedure) {
        LocalDate date = procedure.getStarDateTime().toLocalDate();
        LocalTime startTime = procedure.getStarDateTime().toLocalTime();

        if (this.getCalendar().containsKey(date)) {
            this.getCalendar().get(date).remove(startTime);
            
            if (this.getCalendar().get(date).isEmpty()) {
                this.getCalendar().remove(date);
            }
        }
    }
}