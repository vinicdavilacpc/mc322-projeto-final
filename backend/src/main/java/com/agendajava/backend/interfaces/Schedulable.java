package com.agendajava.backend.interfaces;

import java.time.Duration;
import java.time.LocalDateTime;

import com.agendajava.backend.model.Calendar;
import com.agendajava.backend.model.procedures.Procedure;

/***
 * Interface que realiza o agendamento de procedimentos. Permite verificar a disponibilidade
 * de um recurso em seu respectivo calendário e fazer o agendamento caso seja possível.
 */
public interface Schedulable {

    /* É importante pensar bem nesse método de disponibilidade considerando que não pode haver overlap na duração
    de procedimentos distintos... */
    public boolean isAvailable(LocalDateTime startDateTime, Duration duration);
    public void schedule(LocalDateTime startDateTime, Duration duration, Procedure procedure);
}
