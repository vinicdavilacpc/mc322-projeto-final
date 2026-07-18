package com.agendajava.backend.model;

import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.SurgeryRoom;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

class SurgeryManagerTest {

    @Mock
    private DoctorManager mockDoctorManager;
    @Mock
    private DataManager mockDataManager;

    private SurgeryManager surgeryManagerUnderTest;

    @BeforeEach
    void setUp() throws Exception {
        initMocks(this);
        surgeryManagerUnderTest = new SurgeryManager(0, 0, mockDoctorManager, mockDataManager);
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
                Manager.Specialty.ANESTESIOLOGIA, Manager.Priority.ELETIVA, false, Duration.ofDays(0L), 0,
                Duration.ofDays(0L), LocalDate.of(2020, 1, 1));

        // Run the test
        surgeryManagerUnderTest.addToPriorityLine(surgery);

        // Verify the results
    }

    @Test
    void testSurgeryScheduler() throws Exception {
        // Setup
        final ArrayList<Surgery> priorityLine = new ArrayList<>(
                List.of(new Surgery("name", new Patient("name", "email", "password"), Manager.Specialty.ANESTESIOLOGIA,
                        Manager.Priority.ELETIVA, false, Duration.ofDays(0L), 0, Duration.ofDays(0L),
                        LocalDate.of(2020, 1, 1))));
        final ArrayList<SurgeryRoom> rooms = new ArrayList<>(
                List.of(new SurgeryRoom("name", List.of(new Equipment("name")))));

        // Configure DoctorManager.getAnestesists(...).
        final List<Doctor> doctors = List.of(
                new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false));
        when(mockDoctorManager.getAnestesists()).thenReturn(doctors);

        // Configure DoctorManager.getSurgeonsOf(...).
        final List<Doctor> doctors1 = List.of(
                new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false));
        when(mockDoctorManager.getSurgeonsOf(Manager.Specialty.ANESTESIOLOGIA)).thenReturn(doctors1);

        when(mockDataManager.getProceduresFile()).thenReturn("PROCEDURES_FILE");

        // Run the test
        final String result = surgeryManagerUnderTest.surgeryScheduler(priorityLine, rooms);

        // Verify the results
        assertEquals("Fila de Cirurgias processada com sucesso! Verifique em Meus Agendamentos.", result);
        verify(mockDataManager).add(eq("PROCEDURES_FILE"), any(Surgery.class));
    }

    @Test
    void testSurgeryScheduler_DoctorManagerGetAnestesistsReturnsNull() throws Exception {
        // Setup
        final ArrayList<Surgery> priorityLine = new ArrayList<>(
                List.of(new Surgery("name", new Patient("name", "email", "password"), Manager.Specialty.ANESTESIOLOGIA,
                        Manager.Priority.ELETIVA, false, Duration.ofDays(0L), 0, Duration.ofDays(0L),
                        LocalDate.of(2020, 1, 1))));
        final ArrayList<SurgeryRoom> rooms = new ArrayList<>(
                List.of(new SurgeryRoom("name", List.of(new Equipment("name")))));
        when(mockDoctorManager.getAnestesists()).thenReturn(null);

        // Configure DoctorManager.getSurgeonsOf(...).
        final List<Doctor> doctors = List.of(
                new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false));
        when(mockDoctorManager.getSurgeonsOf(Manager.Specialty.ANESTESIOLOGIA)).thenReturn(doctors);

        // Run the test
        final String result = surgeryManagerUnderTest.surgeryScheduler(priorityLine, rooms);

        // Verify the results
        assertEquals("Fila de Cirurgias processada com sucesso! Verifique em Meus Agendamentos.", result);
    }

    @Test
    void testSurgeryScheduler_DoctorManagerGetAnestesistsReturnsNoItems() throws Exception {
        // Setup
        final ArrayList<Surgery> priorityLine = new ArrayList<>(
                List.of(new Surgery("name", new Patient("name", "email", "password"), Manager.Specialty.ANESTESIOLOGIA,
                        Manager.Priority.ELETIVA, false, Duration.ofDays(0L), 0, Duration.ofDays(0L),
                        LocalDate.of(2020, 1, 1))));
        final ArrayList<SurgeryRoom> rooms = new ArrayList<>(
                List.of(new SurgeryRoom("name", List.of(new Equipment("name")))));
        when(mockDoctorManager.getAnestesists()).thenReturn(Collections.emptyList());

        // Configure DoctorManager.getSurgeonsOf(...).
        final List<Doctor> doctors = List.of(
                new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false));
        when(mockDoctorManager.getSurgeonsOf(Manager.Specialty.ANESTESIOLOGIA)).thenReturn(doctors);

        // Run the test
        final String result = surgeryManagerUnderTest.surgeryScheduler(priorityLine, rooms);

        // Verify the results
        assertEquals("Fila de Cirurgias processada com sucesso! Verifique em Meus Agendamentos.", result);
    }

    @Test
    void testSurgeryScheduler_DoctorManagerGetSurgeonsOfReturnsNull() throws Exception {
        // Setup
        final ArrayList<Surgery> priorityLine = new ArrayList<>(
                List.of(new Surgery("name", new Patient("name", "email", "password"), Manager.Specialty.ANESTESIOLOGIA,
                        Manager.Priority.ELETIVA, false, Duration.ofDays(0L), 0, Duration.ofDays(0L),
                        LocalDate.of(2020, 1, 1))));
        final ArrayList<SurgeryRoom> rooms = new ArrayList<>(
                List.of(new SurgeryRoom("name", List.of(new Equipment("name")))));

        // Configure DoctorManager.getAnestesists(...).
        final List<Doctor> doctors = List.of(
                new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false));
        when(mockDoctorManager.getAnestesists()).thenReturn(doctors);

        when(mockDoctorManager.getSurgeonsOf(Manager.Specialty.ANESTESIOLOGIA)).thenReturn(null);

        // Run the test
        final String result = surgeryManagerUnderTest.surgeryScheduler(priorityLine, rooms);

        // Verify the results
        assertEquals("Fila de Cirurgias processada com sucesso! Verifique em Meus Agendamentos.", result);
    }

    @Test
    void testSurgeryScheduler_DoctorManagerGetSurgeonsOfReturnsNoItems() throws Exception {
        // Setup
        final ArrayList<Surgery> priorityLine = new ArrayList<>(
                List.of(new Surgery("name", new Patient("name", "email", "password"), Manager.Specialty.ANESTESIOLOGIA,
                        Manager.Priority.ELETIVA, false, Duration.ofDays(0L), 0, Duration.ofDays(0L),
                        LocalDate.of(2020, 1, 1))));
        final ArrayList<SurgeryRoom> rooms = new ArrayList<>(
                List.of(new SurgeryRoom("name", List.of(new Equipment("name")))));

        // Configure DoctorManager.getAnestesists(...).
        final List<Doctor> doctors = List.of(
                new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false));
        when(mockDoctorManager.getAnestesists()).thenReturn(doctors);

        when(mockDoctorManager.getSurgeonsOf(Manager.Specialty.ANESTESIOLOGIA)).thenReturn(Collections.emptyList());

        // Run the test
        final String result = surgeryManagerUnderTest.surgeryScheduler(priorityLine, rooms);

        // Verify the results
        assertEquals("Fila de Cirurgias processada com sucesso! Verifique em Meus Agendamentos.", result);
    }
}
