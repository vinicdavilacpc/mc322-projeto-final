package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.agendajava.backend.model.Manager.Priority;
import com.agendajava.backend.model.Manager.Specialty;
import com.agendajava.backend.model.rooms.SurgeryRoom;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;

public class Surgery extends Procedure {
    // Atributos agendados
    private Doctor surgeon;              // Cirurgião responsável
    private SurgeryRoom room;            // Sala de cirurgia alocada
    private LocalDateTime startDateTime; // Horário de início da cirurgia

    // Atributos qualitativos
    private Specialty specialty;  // Especialidade da cirurgia
    private Priority priority;    // Prioridade da cirurgia
    private boolean icuNecessity; // Necessidade de UTI pós-operatória
    // Obs: Specialty e Priority são do tipo Enum

    // Atributos quantitativos
    public Duration turnoverTime = Duration.ofMinutes(30); // Tempo de limpeza e preparação da sala médio para uma clínica
    private int clinicalPriority;                          // Score de 1 a 3 com a prioridade da cirurgia (esse score é um complemento à Priority)
    private Duration estimatedRecoverDuration;             // Tempo de recuperação (exige 1 leito de recuperação por esse período)
    private LocalDate limitDate;                           // Prazo limite para a cirurgia

    @JsonCreator
    public Surgery (
            @JsonProperty("name") String name, 
            @JsonProperty("patient") Patient patient,
            @JsonProperty("specialty") Specialty specialty,
            @JsonProperty("priority") Priority priority,
            @JsonProperty("icuNecessity") boolean icuNecessity;
            @JsonProperty("estimatedDuration") Duration duration,
            @JsonProperty("clinicalPriority") int clinicalPriority,
            @JsonProperty("estimatedRecoverDuration") Duration estimatedRecoverDuration,
            @JsonProperty("limitDate") LocalDate limitDate) {

        estimatedDuration = duration.plus(turnoverTime); // A duração estimada total da cirurgia envolve o tempo de turnover
        super(name, null, estimatedDuration, patient);
        this.specialty = specialty;
        this.priority = priority;
        this.icuNecessity = icuNecessity;
        this.clinicalPriority = clinicalPriority;
        this.estimatedRecoverDuration = estimatedRecoverDuration;
        this.limitDate = limitDate;
    }

    public boolean isEmergency() {
        if (priority.equals(Priority.EMERGENCIA))
            return true;
        return false;
    }

    public boolean isUrgency() {
        if (priority.equals(Priority.URGENCIA))
            return true;
        return false;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public LocalDateTime getLimitDate() {
        return limitDate;
    }

    public boolean needsICU() {
        return icuNecessity;
    }

    public Duration getICURecoverTime() {
        return estimatedRecoverDuration;
    }

    public int getClinicalPriority() {
        return clinicalPriority;
    }

    public void setSurgeon(Doctor surgeon) {
        this.surgeon = surgeon;
    }

    public void setRoom(SurgeryRoom room) {
        this.room = room;
    }

    public void setStart (LocalDateTime starDateTime) {
        this.starDateTime = starDateTime;
    }
}
