package com.agendajava.backend.model.users;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.TreeMap;

import com.agendajava.backend.interfaces.Schedulable;
import com.agendajava.backend.model.SchedulingConflictException;
import com.agendajava.backend.model.procedures.Procedure;

public class Doctor extends User implements Schedulable {
    String specialty;

    public Doctor(String name, String email, String password, String specialty) {
        super(name, email, password);
        this.specialty = specialty;
    }

    public String getSpecialty() {
        return this.specialty;
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
            throw new SchedulingConflictException ( // Ver como criar exception!!!
                "Horário indisponível!"
            );
        } 

        daymap.put(startTime, procedure);
    }
}
