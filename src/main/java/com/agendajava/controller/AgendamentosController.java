package com.agendajava.controller;

import java.util.List;

import com.agendajava.backend.model.Manager;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class AgendamentosController {

    @FXML
    private ListView<String> listaAgendamentos;

    private Manager manager;

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public void carregarAgendamentos() {
        listaAgendamentos.getItems().clear();
        List<String> meusAgendamentos = manager.getMeusProcedimentosFormatados();
        if (meusAgendamentos.isEmpty()) {
            listaAgendamentos.getItems().add("Nenhum agendamento encontrado.");
        } else {
            listaAgendamentos.getItems().addAll(meusAgendamentos);
        }
    }
}