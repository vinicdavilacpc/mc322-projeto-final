package com.agendajava.controller;

import com.agendajava.backend.model.Manager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML
    private TextField emailInput;

    @FXML
    private PasswordField senhaInput;

    @FXML
    private Label mensagemErro;

    private final Manager manager = new Manager();

    @FXML
    public void fazerLogin() {
        String email = emailInput.getText();
        String senha = senhaInput.getText();
        String resultado = manager.loginSuccessful(email, senha);

        if (resultado.equals("Login sucessful")) {
            mensagemErro.setText("Sucesso! Entrando no sistema...");
            mensagemErro.setStyle("-fx-text-fill: green;");
            
        } else {
            mensagemErro.setText(resultado);
            mensagemErro.setStyle("-fx-text-fill: red;");
        }
    }
}