package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.agendajava.backend.model.Manager.Priority;
import com.agendajava.backend.model.Manager.Specialty;
import com.agendajava.backend.model.TimeBlock;
import com.agendajava.backend.model.rooms.SurgeryRoom;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;

public class Surgery extends Procedure {
    private Doctor doctor;
    private SurgeryRoom room;    // ID da sala alocada 
    private TimeBlock timeBlock; // Bloco de horário alocado

    // Atributos quantitativos
    private Duration estimatedDuration;    // Duração estimada (em minutos)
    private int turnoverTime;         // Tempo de limpeza e preparação da sala (em minutos) --> preset
    private int clinicalPriority;     // Score de 1 a 5 com a prioridade da cirurgia
    private int estimatedRecoverTime; // Tempo de recuperação (exige 1 leito de recuperação por esse período)
    private LocalDateTime limitDate;  // Prazo limite para a cirurgia

    // Atributos qualitativos
    private Doctor surgeon;                    // ID do cirurgião responsável
    private Specialty specialty;              // Especialidade da cirurgia
    private Priority priority;                // Prioridade da cirurgia
    private boolean icuNecessity;             // Necessidade de UTI pós-operatória

    // Obs: Specialty e Priority são do tipo Enum

    public Surgery (String name, LocalDateTime dateTime, Duration dur, Patient p, Doctor d) {
        super(name, dateTime, dur, p);
        /* this.estimatedDuration = a1;    
        this.turnoverTime = a2;         
        this.clinicalPriority = a3;     
        this.estimatedRecoverTime = a4; 
        this.limitDate = a5;  
        this.surgeonID = a6;                   
        this.specialty = a7;              
        this.necessaryEquipments = a8; 
        this.priority = a9;                
        this.icuNecessity = a10; */
        // DEFINIÇÃO DOS OUTROS ATRIBUTOS!!!
        this.doctor = d;
    }

    public boolean isUrgent() {
        if (priority.equals(Priority.URGENCIA) || priority.equals(Priority.EMERGENCIA))
            return true;
        return false;
    }

    public LocalDateTime getLimitDate() {
        return limitDate;
    }

    public boolean needsICU() {
        return icuNecessity;
    }

}
