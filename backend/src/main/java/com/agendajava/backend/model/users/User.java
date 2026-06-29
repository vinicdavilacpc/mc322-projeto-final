package com.agendajava.backend.model.users;

import com.agendajava.backend.model.Calendar;

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

    
}
