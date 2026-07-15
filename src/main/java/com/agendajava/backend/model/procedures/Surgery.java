package com.agendajava.backend.model.procedures;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.agendajava.backend.BackendApplication.Priority;
import com.agendajava.backend.BackendApplication.Specialty;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;

public class Surgery extends Procedure {
    private Doctor doctor;
    int roomID;    // ID da sala alocada 
    int blockTime; // Bloco de horário alocado

    // Atributos quantitativos
    int estimatedDuration;    // Duração estimada (em minutos)
    int turnoverTime;         // Tempo de limpeza e preparação da sala (em minutos) --> preset
    int clinicalPriority;     // Score de 1 a 5 com a prioridade da cirurgia
    int estimatedRecoverTime; // Tempo de recuperação (exige 1 leito de recuperação por esse período)
    LocalDateTime limitDate;  // Prazo limite para a cirurgia

    // Atributos qualitativos
    int surgeonID;                    // ID do cirurgião responsável
    Specialty specialty;              // Especialidade da cirurgia
    List<String> necessaryEquipments; // Equipamentos necessários na sala
    Priority priority;                // Prioridade da cirurgia
    boolean icuNecessity;             // Necessidade de UTI pós-operatória
    boolean anestesistNecessity;      // Necessidade de anestesista

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

    

}
