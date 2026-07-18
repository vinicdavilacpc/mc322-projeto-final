package com.agendajava.backend.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataManagerTest {

    private DataManager dataManagerUnderTest;

    @BeforeEach
    void setUp() {
        dataManagerUnderTest = new DataManager();
    }

    @Test
    void testGetUsersFile() {
        assertEquals("users.json", dataManagerUnderTest.getUsersFile());
    }

    @Test
    void testGetProceduresFile() {
        assertEquals("procedures.json", dataManagerUnderTest.getProceduresFile());
    }

    @Test
    void testGetRoomsFile() {
        assertEquals("rooms.json", dataManagerUnderTest.getRoomsFile());
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
        assertEquals("result", result);
    }

    @Test
    void testFindAll() {
        // Setup
        // Run the test
        final List<String> result = dataManagerUnderTest.findAll("fileName", String.class);

        // Verify the results
        assertEquals(List.of("value"), result);
    }
}
