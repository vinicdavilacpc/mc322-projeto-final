package com.agendajava.backend.model.procedures;

import com.agendajava.backend.model.users.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.MockitoAnnotations.initMocks;

class ProcedureTest {

    @Mock
    private Patient mockPatient;

    private Procedure procedureUnderTest;

    @BeforeEach
    void setUp() throws Exception {
        initMocks(this);
        procedureUnderTest = new Procedure("name", LocalDateTime.of(2020, 1, 1, 0, 0, 0), Duration.ofDays(0L),
                mockPatient) {};
    }

    @Test
    void testGetName() {
        assertEquals("name", procedureUnderTest.getName());
    }

    @Test
    void testStartDateTimeGetterAndSetter() {
        final LocalDateTime startDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        procedureUnderTest.setStartDateTime(startDateTime);
        assertEquals(startDateTime, procedureUnderTest.getStarDateTime());
    }

    @Test
    void testGetDuration() {
        assertEquals(Duration.ofDays(0L), procedureUnderTest.getDuration());
    }

    @Test
    void testGetPatient() {
        assertEquals(mockPatient, procedureUnderTest.getPatient());
    }

    @Test
    void testGetEndDateTime() {
        // Setup
        // Run the test
        final LocalDateTime result = procedureUnderTest.getEndDateTime();

        // Verify the results
        assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0, 0), result);
    }

    @Test
    void testOverlapsWith() {
        // Setup
        final Duration dur = Duration.ofDays(0L);

        // Run the test
        final boolean result = procedureUnderTest.overlapsWith(LocalDateTime.of(2020, 1, 1, 0, 0, 0), dur);

        // Verify the results
        assertFalse(result);
    }
}
