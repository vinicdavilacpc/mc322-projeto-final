package com.agendajava.backend.model;

import com.agendajava.backend.model.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class AuthenticatorTest {

    private Authenticator authenticatorUnderTest;

    @BeforeEach
    void setUp() {
        authenticatorUnderTest = new Authenticator();
    }

    @Test
    void testLogin() {
        // Setup
        final DataManager dataManager = new DataManager();

        // Run the test
        final User result = authenticatorUnderTest.login("email", "password", dataManager);

        // Verify the results
    }

    @Test
    void testRegister() {
        // Setup
        final DataManager dataManager = new DataManager();

        // Run the test
        final User result = authenticatorUnderTest.register("name", "email", "password", "role",
                Manager.Specialty.ANESTESIOLOGIA, dataManager);

        // Verify the results
    }

    @Test
    void testUserExists() {
        // Setup
        final DataManager dataManager = new DataManager();

        // Run the test
        final boolean result = authenticatorUnderTest.userExists("email", dataManager);

        // Verify the results
        assertFalse(result);
    }
}
