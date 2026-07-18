package com.agendajava.backend.model.rooms;

import com.agendajava.backend.model.procedures.Procedure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ICURoomTest {

    private ICURoom icuRoomUnderTest;

    @BeforeEach
    void setUp() {
        icuRoomUnderTest = new ICURoom("name", 0);
    }

    @Test
    void testHasBedsAvailable() {
        // Setup
        final Duration duration = Duration.ofDays(0L);

        // Run the test
        final boolean result = icuRoomUnderTest.hasBedsAvailable(LocalDateTime.of(2020, 1, 1, 0, 0, 0), duration);

        // Verify the results
        assertFalse(result);
    }

    @Test
    void testBedSchedule() {
        // Setup
        final Duration duration = Duration.ofDays(0L);
        final Procedure procedure = null;

        // Run the test
        icuRoomUnderTest.bedSchedule(LocalDateTime.of(2020, 1, 1, 0, 0, 0), duration, procedure);

        // Verify the results
    }

    @Test
    void testAddBed() {
        // Setup
        // Run the test
        icuRoomUnderTest.addBed();

        // Verify the results
    }

    @Test
    void testRemoveBed() {
        // Setup
        // Run the test
        icuRoomUnderTest.removeBed();

        // Verify the results
    }

    @Test
    void testGetBedNumber() {
        assertEquals(0, icuRoomUnderTest.getBedNumber());
    }
}
