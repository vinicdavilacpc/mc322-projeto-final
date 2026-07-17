package com.agendajava.backend.model.rooms;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.agendajava.backend.model.Calendar;
import com.agendajava.backend.model.Equipment;
import com.agendajava.backend.model.procedures.Procedure;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Room {
    private final String name;
    private int ID;
    @JsonIgnore
    private Calendar calendar;
    private List<Equipment> equipments = new ArrayList<>();

    public Room(String name, List<Equipment> equipments) {
        this.name = name;
        this.equipments = equipments;
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.ID;
    }

    /* Já retorna o calendário em um formato acessável! */
    @JsonIgnore
    public Map<LocalDate, TreeMap<LocalTime, Procedure>> getCalendar() {
        return this.calendar.get();
    }
}
