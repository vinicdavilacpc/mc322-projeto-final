package com.agendajava.backend.model.rooms;

import com.agendajava.backend.model.procedures.Procedure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class SurgeryRoomTest {

    private SurgeryRoom surgeryRoomUnderTest;

    @BeforeEach
    void setUp() {
        surgeryRoomUnderTest = new SurgeryRoom("name");
    }

    @Test
    void testIsAvailable() {
        // Setup
        final Duration duration = Duration.ofDays(0L);

        // Run the test
        final boolean result = surgeryRoomUnderTest.isAvailable(LocalDateTime.of(2020, 1, 1, 0, 0, 0), duration);

        // Verify the results
        assertThat(result).isFalse();
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
        assertThat(result).isEqualTo(LocalTime.of(0, 0, 0));
    }
}
