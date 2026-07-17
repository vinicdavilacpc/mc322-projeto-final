package com.agendajava.backend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Equipment {
    private String name;
    
    @JsonIgnore
    private Calendar calendar;

    @JsonCreator
    public Equipment(@JsonProperty("name") String name) {
        this.name = name;
        this.calendar = new Calendar();
    }

    public String getName() {
        return this.name;
    }
}