package com.agendajava.backend.interfaces;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.agendajava.backend.model.procedures.Procedure;

public interface Schedulable {
    public boolean isAvailable(LocalDateTime startDateTime, Duration duration);
    public void schedule(LocalDateTime startDateTime, Duration duration, Procedure procedure);
    public void cancel(Procedure procedure);
    public LocalTime nextTimeAvailable(LocalDateTime startDateTime, Duration duration);
}