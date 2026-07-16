package com.agendajava.backend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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