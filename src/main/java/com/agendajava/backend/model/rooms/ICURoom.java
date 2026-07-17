package com.agendajava.backend.model.rooms;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.agendajava.backend.exceptions.SchedulingConflict;
import com.agendajava.backend.model.Calendar;
import com.agendajava.backend.model.procedures.Procedure;

public class ICURoom extends Room {
    private List<Calendar> bedsCalendars;
    private int bedNumber;               

    public ICURoom(String name, int bedNumber) {
        super(name);
        this.bedNumber = bedNumber;
        this.bedsCalendars = new ArrayList<>(bedNumber);
        for (int i = 0; i < bedNumber; i++)
            this.bedsCalendars.add(new Calendar());
    }

    public boolean hasBedsAvailable(LocalDateTime startDateTime, Duration duration) {
        for (int i = 0; i < bedNumber; i++) {
            Calendar bedCalendar = bedsCalendars.get(i);
            if (bedIsAvailable(bedCalendar, startDateTime, duration))
                return true;
        }
        return false;
    }

    public void bedSchedule(LocalDateTime startDateTime, Duration duration, Procedure procedure) {
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        Calendar bedCalendar = null;
        
        for (int i = 0; i < bedNumber; i++) {
            bedCalendar = bedsCalendars.get(i);
            if (bedIsAvailable(bedCalendar, startDateTime, duration))
                break; 
        }

        if (bedCalendar != null) {
            bedCalendar.computeIfAbsent(date, d -> new TreeMap<>());
            TreeMap<LocalTime, Procedure> daymap = bedCalendar.get(date);
            daymap.put(startTime, procedure);
        }
    }

    public void addBed() {
        bedsCalendars.add(new Calendar());
        bedNumber++;
    }

    public void removeBed() {
        bedsCalendars.remove(bedNumber - 1);
        bedNumber--;
    }

    private boolean bedIsAvailable(Calendar bedCalendar, LocalDateTime startDateTime, Duration duration) {
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        bedCalendar.computeIfAbsent(date, d -> new TreeMap<>());
        TreeMap<LocalTime, Procedure> daymap = bedCalendar.get(date);
        
        if (daymap.isEmpty()) return true;
        
        LocalTime priorProcedureStartTime = daymap.floorKey(startTime); 
        if (priorProcedureStartTime != null && daymap.get(priorProcedureStartTime).overlapsWith(startDateTime, duration))
            return false;
            
        LocalTime nextProcedureStartTime = daymap.ceilingKey(startTime); 
        if (nextProcedureStartTime != null && daymap.get(nextProcedureStartTime).overlapsWith(startDateTime, duration))
            return false;
            
        return true;
    }

    public int getBedNumber() {
        return bedNumber;
    }
}