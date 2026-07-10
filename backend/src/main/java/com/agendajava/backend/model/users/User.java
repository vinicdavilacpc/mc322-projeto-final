package com.agendajava.backend.model.users;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.TreeMap;

import com.agendajava.backend.model.Calendar;
import com.agendajava.backend.model.procedures.Procedure;

public abstract class User {
    private String name;
    private String email;
    private String password;
    private Calendar calendar;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.calendar = new Calendar();
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    /* Já retorna o calendário em um formato acessável! */
    public Map<LocalDate, TreeMap<LocalTime, Procedure>> getCalendar() {
        return this.calendar.get();
    }
}
