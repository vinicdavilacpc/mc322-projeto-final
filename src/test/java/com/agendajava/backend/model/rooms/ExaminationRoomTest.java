package com.agendajava.backend.model.rooms;

import com.agendajava.backend.model.procedures.Procedure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExaminationRoomTest {

    private ExaminationRoom examinationRoomUnderTest;

    @BeforeEach
    void setUp() {
        examinationRoomUnderTest = new ExaminationRoom("name", List.of());
    }

    @Test
    void testIsAvailable() {
        // Setup
        final Duration duration = Duration.ofDays(0L);

        // Run the test
        final boolean result = examinationRoomUnderTest.isAvailable(LocalDateTime.of(2020, 1, 1, 0, 0, 0), duration);

        // Verify the results
        assertThat(result).isFalse();
    }

    @Test
    void testSchedule() {
        // Setup
        final Duration duration = Duration.ofDays(0L);
        final Procedure procedure = null;

        // Run the test
        examinationRoomUnderTest.schedule(LocalDateTime.of(2020, 1, 1, 0, 0, 0), duration, procedure);

        // Verify the results
    }

    @Test
    void testCancel() {
        // Setup
        final Procedure procedure = null;

        // Run the test
        examinationRoomUnderTest.cancel(procedure);

        // Verify the results
    }

    @Test
    void testNextTimeAvailable() {
        // Setup
        final Duration duration = Duration.ofDays(0L);

        // Run the test
        final LocalTime result = examinationRoomUnderTest.nextTimeAvailable(LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                duration);

        // Verify the results
        assertThat(result).isEqualTo(LocalTime.of(0, 0, 0));
    }
}
