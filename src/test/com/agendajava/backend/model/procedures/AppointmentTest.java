package com.agendajava.backend.model.procedures;

import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

class AppointmentTest {

    @Mock
    private Patient mockPatient;
    @Mock
    private Doctor mockDoctor;

    private Appointment appointmentUnderTest;

    @BeforeEach
    void setUp() throws Exception {
        initMocks(this);
        appointmentUnderTest = new Appointment("name", LocalDateTime.of(2020, 1, 1, 0, 0, 0), Duration.ofDays(0L),
                mockPatient, mockDoctor);
    }

    @Test
    void testGetDoctor() throws Exception {
        assertEquals(mockDoctor, appointmentUnderTest.getDoctor());
    }
}
