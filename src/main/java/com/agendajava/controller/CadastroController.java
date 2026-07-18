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

public class CadastroController {

    @FXML private TextField nomeInput;
    @FXML private TextField emailInput;
    @FXML private PasswordField senhaInput;
    @FXML private Label mensagemFeedback;

    private Manager manager = new Manager();

    @FXML
    public void cadastrarPaciente() {
        String nome = nomeInput.getText();
        String email = emailInput.getText();
        String senha = senhaInput.getText();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            mensagemFeedback.setText("Preencha todos os campos!");
            mensagemFeedback.setStyle("-fx-text-fill: red;");
            return;
        }

        String resultado = manager.registrationSuccessful(nome, email, senha, "patient", null);

        if (resultado.equals("Registration sucessful")) {
            mensagemFeedback.setText("Cadastro realizado! Voltando ao login...");
            mensagemFeedback.setStyle("-fx-text-fill: green;");
            voltarParaLogin();
        } else {
            mensagemFeedback.setText(resultado);
            mensagemFeedback.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void voltarParaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendajava/view/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) nomeInput.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 450));
            stage.setTitle("Agenda Java - Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}