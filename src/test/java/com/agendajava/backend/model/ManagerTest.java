package com.agendajava.backend.model;

import com.agendajava.backend.model.procedures.Appointment;
import com.agendajava.backend.model.procedures.Examination;
import com.agendajava.backend.model.procedures.Procedure;
import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.ExaminationRoom;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import com.agendajava.backend.model.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManagerTest {

    private Manager managerUnderTest;

    @BeforeEach
    void setUp() {
        managerUnderTest = new Manager();
    }

    @Test
    void testGetDoctorByEmail() {
        // Setup
        // Run the test
        final Doctor result = managerUnderTest.getDoctorByEmail("email");

        // Verify the results
    }

    @Test
    void testGetExaminationRoomByName() {
        // Setup
        // Run the test
        final ExaminationRoom result = managerUnderTest.getExaminationRoomByName("name");

        // Verify the results
    }

    @Test
    void testLoginSuccessful() {
        // Setup
        // Run the test
        final String result = managerUnderTest.loginSuccessful("email", "password");

        // Verify the results
        assertEquals("Login sucessful", result);
    }

    @Test
    void testRegistrationSuccessful() {
        // Setup
        // Run the test
        final String result = managerUnderTest.registrationSuccessful("name", "email", "password", "role",
                Manager.Specialty.ANESTESIOLOGIA);

        // Verify the results
        assertEquals("Registration sucessful", result);
    }

    @Test
    void testGetCurrentUser() {
        final User result = managerUnderTest.getCurrentUser();
    }

    @Test
    void testAppointmentScheduled() {
        // Setup
        final Duration duration = Duration.ofDays(0L);
        final Patient patient = new Patient("name", "email", "password");
        final Doctor doctor = new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false);

        // Run the test
        final String result = managerUnderTest.appointmentScheduled("name", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                duration, patient, doctor);

        // Verify the results
        assertEquals("Appointment scheduled", result);
    }

    @Test
    void testExaminationScheduled() {
        // Setup
        final Duration duration = Duration.ofDays(0L);
        final Patient patient = new Patient("name", "email", "password");
        final ExaminationRoom room = new ExaminationRoom("name", List.of(new Equipment("name")));

        // Run the test
        final String result = managerUnderTest.examinationScheduled("name", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                duration, patient, room);

        // Verify the results
        assertEquals("Apenas médicos podem agendar exames.", result);
    }

    @Test
    void testSurgeryCreated() {
        // Setup
        final Patient patient = new Patient("name", "email", "password");
        final Duration duration = Duration.ofDays(0L);
        final Duration estimatedRecoverDuration = Duration.ofDays(0L);

        // Run the test
        final String result = managerUnderTest.surgeryCreated("name", patient, Manager.Specialty.ANESTESIOLOGIA,
                Manager.Priority.ELETIVA, false, duration, 0, estimatedRecoverDuration, LocalDate.of(2020, 1, 1));

        // Verify the results
        assertEquals("Patients cannot create surgeries", result);
    }

    @Test
    void testAppointmentCanceled() {
        // Setup
        final Appointment appointment = new Appointment("name", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                Duration.ofDays(0L), new Patient("name", "email", "password"),
                new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false));

        // Run the test
        final String result = managerUnderTest.appointmentCanceled(appointment);

        // Verify the results
        assertEquals("Appointment canceled", result);
    }

    @Test
    void testExaminationCanceled() {
        // Setup
        final Examination examination = new Examination("name", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                Duration.ofDays(0L), new Patient("name", "email", "password"),
                new ExaminationRoom("name", List.of(new Equipment("name"))));

        // Run the test
        final String result = managerUnderTest.examinationCanceled(examination);

        // Verify the results
        assertEquals("Doctors cannot cancel examinations", result);
    }

    @Test
    void testGetMeusProcedimentos() {
        // Setup
        // Run the test
        final List<Procedure> result = managerUnderTest.getMeusProcedimentos();

        // Verify the results
    }

    @Test
    void testGetPatientByEmail() {
        // Setup
        // Run the test
        final Patient result = managerUnderTest.getPatientByEmail("email");

        // Verify the results
    }

    @Test
    void testProcessarFilaCirurgias() {
        // Setup
        // Run the test
        final String result = managerUnderTest.processarFilaCirurgias();

        // Verify the results
        assertEquals("result", result);
    }

    @Test
    void testSurgeryCanceled() {
        // Setup
        final Surgery surgery = new Surgery("name", new Patient("name", "email", "password"),
                Manager.Specialty.ANESTESIOLOGIA, Manager.Priority.ELETIVA, false, Duration.ofDays(0L), 0,
                Duration.ofDays(0L), LocalDate.of(2020, 1, 1));

        // Run the test
        final String result = managerUnderTest.surgeryCanceled(surgery);

        // Verify the results
        assertEquals("Cirurgia cancelada com sucesso", result);
    }

    @Test
    void testGetTodosMedicos() {
        // Setup
        // Run the test
        final List<Doctor> result = managerUnderTest.getTodosMedicos();

        // Verify the results
    }

    @Test
    void testGetTodosPacientes() {
        // Setup
        // Run the test
        final List<Patient> result = managerUnderTest.getTodosPacientes();

        // Verify the results
    }
}
