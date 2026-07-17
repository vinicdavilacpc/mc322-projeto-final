package com.agendajava.backend.model;

import java.time.LocalTime;
import java.time.DayOfWeek;

/***
 * Classe que representa um bloco de horário para o agendamento de cirurgias. 
 * Contém um dia da semana, horário de início e horário de término.
 * TimeBlock
 */
public class TimeBlock {
    private Map<DayOfWeek, List<LocalTime>> timeByDay;

    public TimeBlock (DayOfWeek dayOfWeek, LocalTime start, LocalTime end) {
        this.dayOfWeek = dayOfWeek;
        this.start = start;
        this.end = end;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }
}