package com.agendajava.controller;

import java.io.IOException;

import com.agendajava.backend.model.Manager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;

public class DashboardController {

    @FXML
    private BorderPane painelPrincipal;

    private Manager manager;

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @FXML
    public void abrirTelaMarcarConsulta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendajava/view/consulta.fxml"));
            Parent telaConsulta = loader.load();
            ConsultaController controller = loader.getController();
            controller.setManager(this.manager);
            painelPrincipal.setCenter(telaConsulta);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirTelaAgendamentos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendajava/view/agendamentos.fxml"));
            Parent telaAgendamentos = loader.load();
            AgendamentosController controller = loader.getController();
            controller.setManager(this.manager);
            controller.carregarAgendamentos();
            painelPrincipal.setCenter(telaAgendamentos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirTelaMarcarExame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendajava/view/exame.fxml"));
            Parent telaExame = loader.load();
            ExameController controller = loader.getController();
            controller.setManager(this.manager);
            painelPrincipal.setCenter(telaExame);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    public void abrirTelaMarcarCirurgia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendajava/view/cirurgia.fxml"));
            Parent telaCirurgia = loader.load();
            CirurgiaController controller = loader.getController();
            controller.setManager(this.manager);
            painelPrincipal.setCenter(telaCirurgia);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void processarFilaCirurgias() {
        String resultado = manager.processarFilaCirurgias();
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Processamento de Cirurgias");
        alert.setHeaderText(null);
        alert.setContentText(resultado);
        alert.showAndWait();
    }

    @FXML
    public void fazerLogout() {
        System.exit(0);
    }
}