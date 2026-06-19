package com.agendajava.backend.model.users;

public class Doctor extends User {
    String specialty;

    public Doctor(String name, String email, String password, String specialty) {
        super(name, email, password);
        this.specialty = specialty;
    }
}
