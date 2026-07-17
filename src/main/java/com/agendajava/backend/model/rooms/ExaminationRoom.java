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

public class ExaminationRoom extends Room implements Schedulable {

    public ExaminationRoom(String name) {
        super(name);
    }

    public boolean isAvailable(LocalDateTime startDateTime, Duration duration) {
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();

        this.getCalendar().computeIfAbsent(date, d -> new TreeMap<>());
        TreeMap<LocalTime, Procedure> daymap = this.getCalendar().get(date);

        if (daymap.isEmpty()) // Não existe nenhum procedimento agendado nesse dia!
            return true;

        LocalTime priorProcedureStartTime = daymap.floorKey(startTime); // Horário de início do procedimento que começa antes do novo
        if (priorProcedureStartTime != null && daymap.get(priorProcedureStartTime).overlapsWith(startDateTime, duration))
            return false;

        LocalTime nextProcedureStartTime = daymap.ceilingKey(startTime); // Horário de início do procedimento que começa depois do novo
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
            throw new SchedulingConflict ( 
                "Horário indisponível!"
            );
        } 

        daymap.put(startTime, procedure);
    }

    public void cancel(Procedure procedure) {
        LocalDateTime startDateTime = procedure.getStarDateTime();
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();

        TreeMap<LocalTime, Procedure> daymap = this.getCalendar().get(date);
        daymap.remove(startTime);
    }

    public LocalTime nextTimeAvailable(LocalDateTime startDateTime, Duration duration) {
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();

        this.getCalendar().computeIfAbsent(date, d -> new TreeMap<>());
        TreeMap<LocalTime, Procedure> daymap = this.getCalendar().get(date);

        if (daymap.isEmpty()) // Não existe nenhum procedimento agendado nesse dia!
            return true;

        LocalTime priorProcedureStartTime = daymap.floorKey(startTime); // Horário de início do procedimento que começa antes do novo
        if (priorProcedureStartTime != null && daymap.get(priorProcedureStartTime).overlapsWith(startDateTime, duration)) {
            Duration priorProcedureDuration = daymap.get(priorProcedureStartTime).getDuration();
            return priorProcedureStartTime.plus(priorProcedureDuration);
        }

        LocalTime nextProcedureStartTime = daymap.ceilingKey(startTime); // Horário de início do procedimento que começa depois do novo
        if (nextProcedureStartTime != null && daymap.get(nextProcedureStartTime).overlapsWith(startDateTime, duration)) {
            Duration nextProcedureDuration = daymap.get(nextProcedureStartTime).getDuration();
            return nextProcedureStartTime.plus(nextProcedureDuration);
        }
    }
}
