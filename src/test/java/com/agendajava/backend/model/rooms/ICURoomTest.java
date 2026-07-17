package com.agendajava.backend.model.rooms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ICURoomTest {

    private ICURoom icuRoomUnderTest;

    @BeforeEach
    void setUp() {
        icuRoomUnderTest = new ICURoom("name", 0);
    }

    @Test
    void testHasBedsAvailable() {
        // Setup
        final LocalDateTime startDateTime = null;
        final Duration duration = null;

        // Run the test
        final boolean result = icuRoomUnderTest.hasBedsAvailable(startDateTime, duration);

        // Verify the results
        assertThat(result).isFalse();
    }

    @Test
    void testBedSchedule() {
        // Setup
        final LocalDateTime startDateTime = null;
        final Duration duration = null;
        final Procedure procedure = null;

        // Run the test
        icuRoomUnderTest.bedSchedule(startDateTime, duration, procedure);

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
        assertThat(icuRoomUnderTest.getBedNumber()).isEqualTo(0);
    }
}
