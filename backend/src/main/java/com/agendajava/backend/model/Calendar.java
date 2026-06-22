package com.agendajava.backend.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.agendajava.backend.model.procedures.Procedure;

/***
 * Classe que associa datas a procedimentos. Classes de recursos como médicos, pacientes, salas e equipamentos
 * implementam calendários como seus atributos. A função dos objetos dessa classe são organizar os procedimentos
 * em uma estrutura de dados que facilite a busca por data (aqui é utilizado um hashmap com um treemap). Dessa 
 * forma, através da interface *schedulable*, é possível verificar a disponibilidade de todos os recursos de um 
 * procedimento e realizar o agendamento caso seja possível.
 */
public class Calendar {

    // Estrutura de busca: data -> (horário de início -> procedimento)
    private final Map<LocalDate, TreeMap<LocalTime, Procedure>> agenda = new HashMap<>();

    public Calendar(){}

    // Os únicos métodos que fazem sentido estar nessa classe são os de listagem de procedimentos, caso achemos interessante...
}