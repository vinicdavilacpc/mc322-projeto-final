package com.agendajava.backend.model;

import com.agendajava.backend.model.procedures.Appointment;
import com.agendajava.backend.model.procedures.Examination;
import com.agendajava.backend.model.procedures.Procedure;
import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.ExaminationRoom;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(result).isEqualTo("Login sucessful");
    }

    @Test
    void testRegistrationSuccessful() {
        // Setup
        final DataManager dataManager = new DataManager();

        // Run the test
        final String result = managerUnderTest.registrationSuccessful("name", "email", "password", "role",
                Manager.Specialty.ANESTESIOLOGIA, dataManager);

        // Verify the results
        assertThat(result).isEqualTo("Registration sucessful");
    }

    @Test
    void testAppointmentScheduled() {
        // Setup
        final Duration duration = Duration.ofDays(0L);
        final Doctor doctor = new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false);

        // Run the test
        final String result = managerUnderTest.appointmentScheduled("name", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                duration, doctor);

        // Verify the results
        assertThat(result).isEqualTo("Appointment scheduled");
    }

    @Test
    void testExaminationScheduled() {
        // Setup
        final Duration duration = Duration.ofDays(0L);
        final ExaminationRoom room = new ExaminationRoom("name", List.of());

        // Run the test
        final String result = managerUnderTest.examinationScheduled("name", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                duration, room);

        // Verify the results
        assertThat(result).isEqualTo("Doctors cannot schedule examinations");
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
        assertThat(result).isEqualTo("Patients cannot create surgeries");
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
        assertThat(result).isEqualTo("Appointment canceled");
    }

    @Test
    void testExaminationCanceled() {
        // Setup
        final Examination examination = new Examination("name", LocalDateTime.of(2020, 1, 1, 0, 0, 0),
                Duration.ofDays(0L), new Patient("name", "email", "password"), new ExaminationRoom("name", List.of()));

        // Run the test
        final String result = managerUnderTest.examinationCanceled(examination);

        // Verify the results
        assertThat(result).isEqualTo("Doctors cannot cancel examinations");
    }

    @Test
    void testSurgeryCanceled() {
        assertThat(managerUnderTest.surgeryCanceled(
                new Surgery("name", new Patient("name", "email", "password"), Manager.Specialty.ANESTESIOLOGIA,
                        Manager.Priority.ELETIVA, false))).isEqualTo("Em desenvolvimento");
    }

    @Test
    void testGetMeusProcedimentos() {
        // Setup
        // Run the test
        final List<Procedure> result = managerUnderTest.getMeusProcedimentos();

        // Verify the results
    }
}
