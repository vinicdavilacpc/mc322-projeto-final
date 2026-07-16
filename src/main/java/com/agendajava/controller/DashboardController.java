package com.agendajava.controller;

import com.agendajava.backend.model.Manager;

import javafx.fxml.FXML;

public class DashboardController {

    private Manager manager;
    
    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @FXML
    public void abrirTelaAgendamentos() {
        System.out.println("Abrindo agendamentos...");
    }

    @FXML
    public void fazerLogout() {
        System.out.println("Saindo do sistema...");
    }
}