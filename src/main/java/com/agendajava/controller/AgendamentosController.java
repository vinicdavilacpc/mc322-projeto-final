package com.agendajava.controller;

import java.util.List;

import com.agendajava.backend.model.Manager;
import com.agendajava.backend.model.procedures.Appointment;
import com.agendajava.backend.model.procedures.Examination;
import com.agendajava.backend.model.procedures.Procedure;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class AgendamentosController {

    @FXML private ListView<Procedure> listaAgendamentos;
    @FXML private Label mensagemFeedback;

    private Manager manager;

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void carregarAgendamentos() {
        listaAgendamentos.getItems().clear();
        
        listaAgendamentos.setCellFactory(param -> new ListCell<Procedure>() {
            @Override
            protected void updateItem(Procedure item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String tipo = item instanceof Appointment ? "Consulta" : "Exame";
                    setText("[" + tipo + "] " + item.getName() + " | Data: " + item.getStarDateTime().toLocalDate() + 
                            " às " + item.getStarDateTime().toLocalTime() + 
                            " | Duração: " + item.getDuration().toMinutes() + "min");
                }
            }
        });

        List<Procedure> meusAgendamentos = manager.getMeusProcedimentos();
        listaAgendamentos.getItems().addAll(meusAgendamentos);
    }

    @FXML
    public void cancelarSelecionado() {
        Procedure selecionado = listaAgendamentos.getSelectionModel().getSelectedItem();
        
        if (selecionado == null) {
            exibirMensagem("Selecione um procedimento na lista para cancelar.", "red");
            return;
        }

        String resultado = "";
        if (selecionado instanceof Appointment) {
            resultado = manager.appointmentCanceled((Appointment) selecionado);
        } else if (selecionado instanceof Examination) {
            resultado = manager.examinationCanceled((Examination) selecionado);
        } else {
            resultado = "Cancelamento não suportado para este tipo.";
        }

        if (resultado.toLowerCase().contains("canceled")) {
            exibirMensagem("Procedimento cancelado com sucesso!", "green");
            carregarAgendamentos(); 
        } else {
            exibirMensagem(resultado, "red");
        }
    }

    private void exibirMensagem(String texto, String cor) {
        mensagemFeedback.setText(texto);
        mensagemFeedback.setStyle("-fx-text-fill: " + cor + ";");
    }
}