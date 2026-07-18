package com.agendajava.backend.model;

import com.agendajava.backend.model.procedures.Procedure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.TreeMap;

class CalendarTest {

    private Calendar calendarUnderTest;

    @BeforeEach
    void setUp() {
        calendarUnderTest = new Calendar();
    }

    @Test
    void testGet() {
        final Map<LocalDate, TreeMap<LocalTime, Procedure>> result = calendarUnderTest.get();
    }
}
