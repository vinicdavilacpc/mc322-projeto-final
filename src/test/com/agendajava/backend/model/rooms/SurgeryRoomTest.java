package com.agendajava.backend.model.rooms;

import com.agendajava.backend.model.Equipment;
import com.agendajava.backend.model.procedures.Procedure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SurgeryRoomTest {

    private SurgeryRoom surgeryRoomUnderTest;

    @BeforeEach
    void setUp() {
        surgeryRoomUnderTest = new SurgeryRoom("name", List.of(new Equipment("name")));
    }

    @Test
    void testIsAvailable() {
        // Setup
        final Duration duration = Duration.ofDays(0L);

        // Run the test
        final boolean result = surgeryRoomUnderTest.isAvailable(LocalDateTime.of(2020, 1, 1, 0, 0, 0), duration);

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testSchedule() {
        // Setup
        final Duration duration = Duration.ofDays(0L);
        final Procedure procedure = null;

        // Run the test
        surgeryRoomUnderTest.schedule(LocalDateTime.of(2020, 1, 1, 0, 0, 0), duration, procedure);

        // Verify the results
    }

    @Test
    void testCancel() {
        // Setup
        final Procedure procedure = null;

        // Run the test
        surgeryRoomUnderTest.cancel(procedure);

        // Verify the results
    }

    @Test
    void testNextTimeAvailable() {
        // Setup
        final Duration duration = Duration.ofDays(0L);

        // Run the test
        final LocalTime result = surgeryRoomUnderTest.nextTimeAvailable(LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                duration);

        // Verify the results
        assertEquals(LocalTime.of(0, 0, 0), result);
    }
}
