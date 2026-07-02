package com.agendajava.backend.model.rooms;

import java.time.LocalDateTime;

import com.agendajava.backend.interfaces.Schedulable;
import com.agendajava.backend.model.procedures.Procedure;

import ch.qos.logback.core.util.Duration;

public class SurgeryRoom extends Room implements Schedulable {

    public SurgeryRoom() {}

    public boolean isAvailable(LocalDateTime startDateTime, Duration duration) {
        return true;
    }
    public void schedule(LocalDateTime startDateTime, Duration duration, Procedure procedure) {}
}
