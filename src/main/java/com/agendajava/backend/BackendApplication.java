package com.agendajava.backend;

import java.time.LocalDateTime;
import java.util.ArrayList;

import com.agendajava.backend.model.procedures.Surgery;
import com.agendajava.backend.model.rooms.SurgeryRoom;

public class BackendApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BackendApplication.class.getResource("/com/agendajava/view/login.fxml"));
        
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        
        primaryStage.setTitle("Agenda Java - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
