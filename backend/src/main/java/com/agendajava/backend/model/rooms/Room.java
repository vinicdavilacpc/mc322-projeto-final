package com.agendajava.backend.model.rooms;

import java.util.List;

import com.agendajava.backend.model.Calendar;

public class Room {
    private int roomID;
    private Calendar calendar;
    // Pensar em uma maneira de colocar o calendário ou horários alocados
    List<String> equipments;

    public Room() {
    }
}
