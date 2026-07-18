package com.agendajava.backend.model.procedures;

import com.agendajava.backend.model.rooms.ExaminationRoom;
import com.agendajava.backend.model.users.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

class ExaminationTest {

    @Mock
    private Patient mockPatient;
    @Mock
    private ExaminationRoom mockRoom;

    private Examination examinationUnderTest;

    @BeforeEach
    void setUp() throws Exception {
        initMocks(this);
        examinationUnderTest = new Examination("name", LocalDateTime.of(2020, 1, 1, 0, 0, 0), Duration.ofDays(0L),
                mockPatient, mockRoom);
    }

    @Test
    void testGetRoom() {
        assertEquals(mockRoom, examinationUnderTest.getRoom());
    }
}
