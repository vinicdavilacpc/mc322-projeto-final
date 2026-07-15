package com.agendajava.backend;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.SurgeryRoom;

public class BackendApplication extends Application {

    // O método main serve apenas para dar o "start" no JavaFX
    public static void main(String[] args) {
        launch(args);
    } // <-- O main precisa fechar aqui!

    // O método start é obrigatório e precisa ficar no nível da classe
    @Override
    public void start(Stage primaryStage) {
        // Aqui é onde você vai começar a construir as telas (Scenes)
        primaryStage.setTitle("Agenda Java");
        primaryStage.show();
    }
}
