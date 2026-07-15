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

public class Room {
    private String name;
    private Calendar calendar;
    private List<Equipment> equipments = new ArrayList<>();

    public Room(String name) {
        this.name = name;
        // this.equipments = equipments;
    }

    /* Já retorna o calendário em um formato acessável! */
    public Map<LocalDate, TreeMap<LocalTime, Procedure>> getCalendar() {
        return this.calendar.get();
    }
}
