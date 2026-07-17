package com.agendajava.backend.model.rooms;

import com.agendajava.backend.model.procedures.Procedure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.Assertions.assertThat;

class RoomTest {

    private Room roomUnderTest;

    @BeforeEach
    void setUp() {
        roomUnderTest = new Room("name");
    }

    @Test
    void testGetName() {
        assertThat(roomUnderTest.getName()).isEqualTo("name");
    }

    @Test
    void testGetID() {
        assertThat(roomUnderTest.getID()).isEqualTo(0);
    }

    @Test
    void testGetEquipments() {
        // Setup
        // Run the test
        final List<Equipment> result = roomUnderTest.getEquipments();

        // Verify the results
    }

    @Test
    void testGetCalendar() {
        // Setup
        // Run the test
        final Map<LocalDate, TreeMap<LocalTime, Procedure>> result = roomUnderTest.getCalendar();

        // Verify the results
    }
}
