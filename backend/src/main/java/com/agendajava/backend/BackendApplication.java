package com.agendajava.backend;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.SurgeryRoom;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
