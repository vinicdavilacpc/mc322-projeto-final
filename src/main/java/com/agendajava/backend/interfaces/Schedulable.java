package com.agendajava.backend.interfaces;

import java.time.Duration;
import java.time.LocalDateTime;
import com.agendajava.backend.model.procedures.Procedure;

/***
 * Interface que realiza o agendamento de procedimentos. Permite verificar a disponibilidade
 * de um recurso em seu respectivo calendário e fazer o agendamento caso seja possível.
 */
public interface Schedulable {

    public boolean isAvailable(LocalDateTime startDateTime, Duration duration);

    public void schedule(LocalDateTime startDateTime, Duration duration, Procedure procedure);

    public void cancel(Procedure procedure);
}
