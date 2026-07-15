package com.agendajava.backend.model.rooms;

public class ICURoom extends Room {
    private int bedsAvailable;

    public ICURoom(String name, int bedNumber) {
        super(name);
        this.bedsAvailable = bedNumber;
    }

    public boolean hasBedsAvailable() {
        if (bedsAvailable <= 0)
            return false;
        return true;
    }

    public void occupyBed() {
        bedsAvailable--;
    }

    public void freeBed() {
        bedsAvailable++;
    }
}