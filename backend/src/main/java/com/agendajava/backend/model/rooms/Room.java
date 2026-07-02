package com.agendajava.backend.model.rooms;

import java.util.List;

import com.agendajava.backend.model.Calendar;
import com.agendajava.backend.model.Equipment;

public class Room {
    private int roomID;
    private Calendar calendar;
    private List<Equipment> equipments;

    public Room() {
    }
}
