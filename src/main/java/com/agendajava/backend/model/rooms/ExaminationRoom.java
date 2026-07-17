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
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ExaminationRoom extends Room implements Schedulable {

    @JsonCreator
    public ExaminationRoom(
            @JsonProperty("name") String name, 
            @JsonProperty("equipments") List<Equipment> equipments) {
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
            throw new SchedulingConflict("Horário indisponível na sala!");
        }
        daymap.put(startTime, procedure);
    }

    @Override
    public void cancel(Procedure procedure) {
    }
}