package com.agendajava.backend.model;

import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.SurgeryRoom;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SurgeryManagerTest {

    @Mock
    private DoctorManager mockDoctorManager;

    private SurgeryManager surgeryManagerUnderTest;

    @BeforeEach
    void setUp() {
        surgeryManagerUnderTest = new SurgeryManager(0, 0, mockDoctorManager);
    }

    @Test
    void testGetPLineOf() {
        // Setup
        // Run the test
        final List<Surgery> result = surgeryManagerUnderTest.getPLineOf(Manager.Specialty.ANESTESIOLOGIA);

        // Verify the results
    }

    @Test
    void testAddToPriorityLine() {
        // Setup
        final Surgery surgery = new Surgery("name", new Patient("name", "email", "password"),
                Manager.Specialty.ANESTESIOLOGIA, Manager.Priority.ELETIVA, false);

        // Run the test
        surgeryManagerUnderTest.addToPriorityLine(surgery);

        // Verify the results
    }

    @Test
    void testSurgeryScheduler() {
        // Setup
        final ArrayList<Surgery> priorityLine = new ArrayList<>(
                List.of(new Surgery("name", new Patient("name", "email", "password"), Manager.Specialty.ANESTESIOLOGIA,
                        Manager.Priority.ELETIVA, false)));
        final ArrayList<SurgeryRoom> rooms = new ArrayList<>(List.of(new SurgeryRoom("name")));

        // Configure DoctorManager.getAnestesistsOf(...).
        final List<Doctor> doctors = List.of(
                new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false));
        when(mockDoctorManager.getAnestesistsOf(Manager.Specialty.ANESTESIOLOGIA)).thenReturn(doctors);

        // Configure DoctorManager.getSurgeonsOf(...).
        final List<Doctor> doctors1 = List.of(
                new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false));
        when(mockDoctorManager.getSurgeonsOf(Manager.Specialty.ANESTESIOLOGIA)).thenReturn(doctors1);

        // Run the test
        surgeryManagerUnderTest.surgeryScheduler(priorityLine, rooms);

        // Verify the results
    }

    @Test
    void testSurgeryScheduler_DoctorManagerGetAnestesistsOfReturnsNoItems() {
        // Setup
        final ArrayList<Surgery> priorityLine = new ArrayList<>(
                List.of(new Surgery("name", new Patient("name", "email", "password"), Manager.Specialty.ANESTESIOLOGIA,
                        Manager.Priority.ELETIVA, false)));
        final ArrayList<SurgeryRoom> rooms = new ArrayList<>(List.of(new SurgeryRoom("name")));
        when(mockDoctorManager.getAnestesistsOf(Manager.Specialty.ANESTESIOLOGIA)).thenReturn(Collections.emptyList());

        // Configure DoctorManager.getSurgeonsOf(...).
        final List<Doctor> doctors = List.of(
                new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false));
        when(mockDoctorManager.getSurgeonsOf(Manager.Specialty.ANESTESIOLOGIA)).thenReturn(doctors);

        // Run the test
        surgeryManagerUnderTest.surgeryScheduler(priorityLine, rooms);

        // Verify the results
    }

    @Test
    void testSurgeryScheduler_DoctorManagerGetSurgeonsOfReturnsNoItems() {
        // Setup
        final ArrayList<Surgery> priorityLine = new ArrayList<>(
                List.of(new Surgery("name", new Patient("name", "email", "password"), Manager.Specialty.ANESTESIOLOGIA,
                        Manager.Priority.ELETIVA, false)));
        final ArrayList<SurgeryRoom> rooms = new ArrayList<>(List.of(new SurgeryRoom("name")));

        // Configure DoctorManager.getAnestesistsOf(...).
        final List<Doctor> doctors = List.of(
                new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false));
        when(mockDoctorManager.getAnestesistsOf(Manager.Specialty.ANESTESIOLOGIA)).thenReturn(doctors);

        when(mockDoctorManager.getSurgeonsOf(Manager.Specialty.ANESTESIOLOGIA)).thenReturn(Collections.emptyList());

        // Run the test
        surgeryManagerUnderTest.surgeryScheduler(priorityLine, rooms);

        // Verify the results
    }
}
