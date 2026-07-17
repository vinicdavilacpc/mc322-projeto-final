package com.agendajava.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

class DataManagerTest {

    private DataManager dataManagerUnderTest;

    @BeforeEach
    void setUp() {
        dataManagerUnderTest = new DataManager();
    }

    @Test
    void testGetUsersFile() {
        assertThat(dataManagerUnderTest.getUsersFile()).isEqualTo("users.json");
    }

    @Test
    void testGetProceduresFile() {
        assertThat(dataManagerUnderTest.getProceduresFile()).isEqualTo("procedures.json");
    }

    @Test
    void testGetRoomsFile() {
        assertThat(dataManagerUnderTest.getRoomsFile()).isEqualTo("rooms.json");
    }

    @Test
    void testSave1() {
        // Setup
        // Run the test
        dataManagerUnderTest.save("fileName", List.of("value"));

        // Verify the results
    }

    @Test
    void testSave2() {
        // Setup
        // Run the test
        dataManagerUnderTest.save("fileName", List.of("value"), String.class);

        // Verify the results
    }

    @Test
    void testAdd() {
        // Setup
        // Run the test
        dataManagerUnderTest.add("fileName", "object");

        // Verify the results
    }

    @Test
    void testDelete() {
        // Setup
        // Run the test
        dataManagerUnderTest.delete("fileName", "object");

        // Verify the results
    }

    @Test
    void testUpdate() {
        // Setup
        final Predicate<String> filter = val -> {
            return false;
        };

        // Run the test
        dataManagerUnderTest.update("fileName", "updatedObject", filter);

        // Verify the results
    }

    @Test
    void testFindOne() {
        // Setup
        final Predicate<String> filter = val -> {
            return false;
        };

        // Run the test
        final String result = dataManagerUnderTest.findOne("fileName", String.class, filter);

        // Verify the results
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testFindAll() {
        // Setup
        // Run the test
        final List<String> result = dataManagerUnderTest.findAll("fileName", String.class);

        // Verify the results
        assertThat(result).isEqualTo(List.of("value"));
    }
}
