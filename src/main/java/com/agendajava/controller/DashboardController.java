package com.agendajava.controller;

import java.io.IOException;

import com.agendajava.backend.model.Manager;
import com.agendajava.backend.model.users.Patient;
import com.agendajava.backend.model.users.User;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DashboardController {

    @FXML private BorderPane painelPrincipal;
    @FXML private Label labelBemVindo;
    @FXML private Button btnMarcarExame;
    @FXML private Button btnMarcarCirurgia;
    @FXML private Button btnRodarAlgoritmo;

    private Manager manager;

    public void setManager(Manager manager) {
        this.manager = manager;
        
        User usuarioLogado = manager.getCurrentUser();
        if (usuarioLogado != null) {
            
            labelBemVindo.setText("Bem-vindo de volta, " + usuarioLogado.getName() + "!");
            
            if (usuarioLogado instanceof Patient) {
                btnMarcarExame.setVisible(false);
                btnMarcarExame.setManaged(false);
                btnMarcarCirurgia.setVisible(false);
                btnMarcarCirurgia.setManaged(false);
                btnRodarAlgoritmo.setVisible(false);
                btnRodarAlgoritmo.setManaged(false);
            }
        }
        abrirTelaAgendamentos();
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/agendajava/view/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) painelPrincipal.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Agenda Java - Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}