package com.agendajava.backend.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class ClinicalCalendar {

    // Estrutura de busca: data -> (horário de início -> procedimento)
    private final Map<LocalDate, TreeMap<LocalTime, Procedure>> agenda = new HashMap<>();

    public ClinicalCalendar(){}

    public void schedule(Procedure procedure) {
        LocalDate date = procedure.getStartDateTime().toLocalDate();
        LocalTime startTime = procedure.getStartDateTime().toLocalTime();

        // Garante que o dia existe no mapa
        agenda.computeIfAbsent(date, d -> new TreeMap<>());
        TreeMap<LocalTime, Procedure> dayMap = agenda.get(date);

        // Verifica conflito antes de inserir
        /* if (hasConflict(dayMap, procedure)) {
            throw new SchedulingConflictException(
                "Já existe um procedimento nesse horário."
            );
        } */

        dayMap.put(startTime, procedure);
    }
}