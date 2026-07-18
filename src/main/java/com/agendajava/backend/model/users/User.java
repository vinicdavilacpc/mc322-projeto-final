package com.agendajava.backend.model.users;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.TreeMap;

import com.agendajava.backend.exceptions.SchedulingConflict;
import com.agendajava.backend.interfaces.Schedulable;
import com.agendajava.backend.model.Calendar;
import com.agendajava.backend.model.procedures.Procedure;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.PROPERTY, 
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Doctor.class, name = "doctor"),
    @JsonSubTypes.Type(value = Patient.class, name = "patient")
})
public abstract class User implements Schedulable {
    private String name;
    private String email;
    private String password;
    
    @JsonIgnore
    private Calendar calendar;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.calendar = new Calendar();
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    @JsonIgnore
    public Map<LocalDate, TreeMap<LocalTime, Procedure>> getCalendar() {
        return this.calendar.get();
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
            throw new SchedulingConflict("User is unavailable at that time");
        } 
        daymap.put(startTime, procedure);
    }

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

    public LocalTime nextTimeAvailable(LocalDateTime startDateTime, Duration duration) {
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        this.getCalendar().computeIfAbsent(date, d -> new TreeMap<>());
        TreeMap<LocalTime, Procedure> daymap = this.getCalendar().get(date);
        
        if (daymap.isEmpty()) return startTime;
        
        LocalTime priorProcedureStartTime = daymap.floorKey(startTime); 
        if (priorProcedureStartTime != null && daymap.get(priorProcedureStartTime).overlapsWith(startDateTime, duration)) {
            Duration priorProcedureDuration = daymap.get(priorProcedureStartTime).getDuration();
            return priorProcedureStartTime.plus(priorProcedureDuration);
        }
        LocalTime nextProcedureStartTime = daymap.ceilingKey(startTime); 
        if (nextProcedureStartTime != null && daymap.get(nextProcedureStartTime).overlapsWith(startDateTime, duration)) {
            Duration nextProcedureDuration = daymap.get(nextProcedureStartTime).getDuration();
            return nextProcedureStartTime.plus(nextProcedureDuration);
        }
        
        return startTime;
    }
}