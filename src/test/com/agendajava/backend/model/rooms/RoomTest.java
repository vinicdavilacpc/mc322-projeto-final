package com.agendajava.backend.model.rooms;

import com.agendajava.backend.model.Equipment;
import com.agendajava.backend.model.procedures.Procedure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomTest {

    private Room roomUnderTest;

    @BeforeEach
    void setUp() {
        roomUnderTest = new Room("name", List.of(new Equipment("name")));
    }

    @Test
    void testGetName() {
        assertEquals("name", roomUnderTest.getName());
    }

    @Test
    void testGetID() {
        assertEquals(0, roomUnderTest.getID());
    }

    @Test
    void testGetEquipments() {
        final List<Equipment> result = roomUnderTest.getEquipments();
    }

    @Test
    void testGetCalendar() {
        // Setup
        // Run the test
        final Map<LocalDate, TreeMap<LocalTime, Procedure>> result = roomUnderTest.getCalendar();

        // Verify the results
    }
}
