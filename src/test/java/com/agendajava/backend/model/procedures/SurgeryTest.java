package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.mockito.MockitoAnnotations.initMocks;

import com.agendajava.backend.model.Equipment;
import com.agendajava.backend.model.Manager;
import com.agendajava.backend.model.rooms.SurgeryRoom;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;

class SurgeryTest {

    @Mock
    private Patient mockPatient;

    private Surgery surgeryUnderTest;

    @BeforeEach
    void setUp() throws Exception {
        initMocks(this);
        Surgery surgeryUnderTest = new Surgery("name", mockPatient, Manager.Specialty.ORTOPEDIA, Manager.Priority.ELETIVA,
                false, Duration.ofHours(2), 0, Duration.ofHours(1), null);
    }

    @Test
    void testIsEmergency() {
        assertFalse(surgeryUnderTest.isEmergency());
    }

    @Test
    void testIsUrgency() {
        assertFalse(surgeryUnderTest.isUrgency());
    }

    @Test
    void testGetSpecialty() {
        assertEquals(Manager.Specialty.ANESTESIOLOGIA, surgeryUnderTest.getSpecialty());
    }

    @Test
    void testGetLimitDate() {
        assertEquals(LocalDate.of(2020, 1, 1), surgeryUnderTest.getLimitDate());
    }

    @Test
    void testNeedsICU() {
        assertFalse(surgeryUnderTest.needsICU());
    }

    @Test
    void testGetICURecoverTime() {
        assertEquals(Duration.ofDays(0L), surgeryUnderTest.getICURecoverTime());
    }

    @Test
    void testGetClinicalPriority() {
        assertEquals(0, surgeryUnderTest.getClinicalPriority());
    }

    @Test
    void testSurgeonGetterAndSetter() {
        final Doctor surgeon = new Doctor("name", "email", "password", Manager.Specialty.ORTOPEDIA, false);
        surgeryUnderTest.setSurgeon(surgeon);
        assertEquals(surgeon, surgeryUnderTest.getDoctor());
    }

    @Test
    void testRoomGetterAndSetter() {
        final SurgeryRoom room = new SurgeryRoom("name", List.of(new Equipment("name")));
        surgeryUnderTest.setRoom(room);
        assertEquals(room, surgeryUnderTest.getRoom());
    }

    @Test
    void testSetStart() {
        // Setup
        // Run the test
        surgeryUnderTest.setStart(LocalDateTime.of(2020, 1, 1, 0, 0, 0));

        // Verify the results
    }
}
