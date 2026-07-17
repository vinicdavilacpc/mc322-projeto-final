package com.agendajava.controller;

import java.io.IOException;

import com.agendajava.backend.model.Manager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField emailInput;
    @FXML private PasswordField senhaInput;
    @FXML private Label mensagemErro;

    private final Manager manager = new Manager();

    @FXML
    public void fazerLogin() {
        String email = emailInput.getText();
        String senha = senhaInput.getText();

        String resultado = manager.loginSuccessful(email, senha);

        if (resultado.equals("Login sucessful")) {
            irParaDashboard();
        } else {
            mensagemErro.setText(resultado);
            mensagemErro.setStyle("-fx-text-fill: red;");
        }
    }

    private void irParaDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendajava/view/dashboard.fxml"));
            Parent root = loader.load();
            DashboardController dashboardController = loader.getController();
            dashboardController.setManager(this.manager);
            Stage stage = (Stage) emailInput.getScene().getWindow();
            Scene scene = new Scene(root, 800, 600);
            stage.setScene(scene);
            stage.setTitle("Agenda Java - Painel Principal");
        } catch (IOException e) {
            e.printStackTrace();
            mensagemErro.setText("Erro ao carregar a tela principal.");
        }
    }

    @FXML
    public void encerrarPrograma() {
        System.exit(0);
    }
}