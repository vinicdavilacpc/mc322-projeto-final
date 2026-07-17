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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.PROPERTY, 
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ExaminationRoom.class, name = "examinationRoom"),
    @JsonSubTypes.Type(value = SurgeryRoom.class, name = "surgeryRoom")
})
public class Room {
    private String name;
    private int ID;
    
    @JsonIgnore
    private Calendar calendar;
    
    private List<Equipment> equipments = new ArrayList<>();

    public Room(String name, List<Equipment> equipments) {
        this.name = name;
        this.equipments = equipments != null ? equipments : new ArrayList<>();
        this.calendar = new Calendar();
    }

    public String getName() {
        return this.name;
    }

    public int getID() {
        return this.ID;
    }

    public List<Equipment> getEquipments() {
        return this.equipments;
    }

    @JsonIgnore
    public Map<LocalDate, TreeMap<LocalTime, Procedure>> getCalendar() {
        return this.calendar.get();
    }
}