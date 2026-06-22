package com.agendajava.backend;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.SurgeryRoom;

@SpringBootApplication
public class BackendApplication {

	public enum Specialty {
        CARDIOLOGIA, DERMATOLOGIA, GINECOLOGIA, NEUROLOGIA, OFTALMOLOGIA, ORTOPEDIA, PEDIATRIA; // Pode ser alterado!
    }

    public enum Priority {
        ELETIVA, URGENCIA, EMERGENCIA; // Pode ser alterado!
    }

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

    /***
     * Algoritmo que agenda cirurgias de acordo com uma fila de prioridade
     * @return
     */
    public void surgeryScheduler(ArrayList<Surgery> priorityLine, ArrayList<SurgeryRoom> rooms) {}
}
