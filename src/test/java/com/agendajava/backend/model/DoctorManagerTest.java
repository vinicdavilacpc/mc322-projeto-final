package com.agendajava.backend.model;

import com.agendajava.backend.model.users.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class DoctorManagerTest {

    @Mock
    private DataManager mockDataManager;

    private DoctorManager doctorManagerUnderTest;

    @BeforeEach
    void setUp() {
        doctorManagerUnderTest = new DoctorManager(mockDataManager);
    }

    @Test
    void testGetDoctorsOf() {
        // Setup
        // Run the test
        final List<Doctor> result = doctorManagerUnderTest.getDoctorsOf(Manager.Specialty.ANESTESIOLOGIA);

        // Verify the results
    }

    @Test
    void testGetSurgeonsOf() {
        // Setup
        // Run the test
        final List<Doctor> result = doctorManagerUnderTest.getSurgeonsOf(Manager.Specialty.ANESTESIOLOGIA);

        // Verify the results
    }

    @Test
    void testGetAnestesistsOf() {
        // Setup
        // Run the test
        final List<Doctor> result = doctorManagerUnderTest.getAnestesistsOf(Manager.Specialty.ANESTESIOLOGIA);

        // Verify the results
    }

    @Test
    void testAddDoctorOf() {
        // Setup
        final Doctor newDoctor = new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false);

        // Run the test
        doctorManagerUnderTest.addDoctorOf(newDoctor);

        // Verify the results
    }

    @Test
    void testAddSurgeon() {
        // Setup
        final Doctor newSurgeon = new Doctor("name", "email", "password", Manager.Specialty.ANESTESIOLOGIA, false);

        // Run the test
        doctorManagerUnderTest.addSurgeon(newSurgeon);

        // Verify the results
    }
}
