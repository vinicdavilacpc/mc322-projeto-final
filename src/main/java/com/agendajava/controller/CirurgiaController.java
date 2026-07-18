package com.agendajava.controller;

import java.time.Duration;
import java.time.LocalDate;

import com.agendajava.backend.model.Manager;
import com.agendajava.backend.model.Manager.Priority;
import com.agendajava.backend.model.Manager.Specialty;
import com.agendajava.backend.model.users.Patient;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class CirurgiaController {

    @FXML private TextField nomeInput;
    @FXML private TextField pacienteEmailInput;
    @FXML private ComboBox<String> especialidadeInput;
    @FXML private ComboBox<String> prioridadeInput;
    @FXML private TextField duracaoInput;
    @FXML private TextField prioridadeClinicaInput;
    @FXML private TextField tempoRecuperacaoInput;
    @FXML private DatePicker dataLimiteInput;
    @FXML private CheckBox utiInput;
    @FXML private Label mensagemFeedback;

    private Manager manager;

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @FXML
    public void initialize() {
        especialidadeInput.getItems().addAll("CARDIOLOGIA", "NEUROLOGIA", "OFTALMOLOGIA", "ORTOPEDIA");
        prioridadeInput.getItems().addAll("ELETIVA", "URGENCIA", "EMERGENCIA");
    }

    @FXML
    public void cadastrarCirurgia() {
        try {
            String nome = nomeInput.getText();
            String emailPaciente = pacienteEmailInput.getText();
            String especialidade = especialidadeInput.getValue();
            String prioridade = prioridadeInput.getValue();
            String duracaoStr = duracaoInput.getText();
            String prioridadeClinicaStr = prioridadeClinicaInput.getText();
            String recuperacaoStr = tempoRecuperacaoInput.getText();
            LocalDate dataLimite = dataLimiteInput.getValue();
            boolean precisaUti = utiInput.isSelected();

            if (nome.isEmpty() || emailPaciente.isEmpty() || especialidade == null || prioridade == null || 
                duracaoStr.isEmpty() || prioridadeClinicaStr.isEmpty() || recuperacaoStr.isEmpty() || dataLimite == null) {
                exibirMensagem("Preencha todos os campos!", "#e74c3c");
                return;
            }

            long duracaoMinutos = Long.parseLong(duracaoStr);
            long recuperacaoMinutos = Long.parseLong(recuperacaoStr);
            int prioridadeClinica = Integer.parseInt(prioridadeClinicaStr);

            if (duracaoMinutos <= 0 || recuperacaoMinutos < 0) {
                exibirMensagem("Os tempos devem ser valores positivos.", "#e74c3c");
                return;
            }

            if (prioridadeClinica < 1 || prioridadeClinica > 3) {
                exibirMensagem("A prioridade clínica deve ser um score de 1 a 3.", "#e74c3c");
                return;
            }

            Patient patient = manager.getPatientByEmail(emailPaciente);
            if (patient == null) {
                exibirMensagem("Paciente não encontrado no sistema.", "#e74c3c");
                return;
            }

            Specialty spec = Specialty.valueOf(especialidade);
            Priority prio = Priority.valueOf(prioridade);
            Duration duracao = Duration.ofMinutes(duracaoMinutos);
            Duration recuperacao = Duration.ofMinutes(recuperacaoMinutos);

            String resultado = manager.surgeryCreated(nome, patient, spec, prio, precisaUti, duracao, prioridadeClinica, recuperacao, dataLimite);

            if (resultado.equals("Surgery created")) {
                exibirMensagem("Cirurgia adicionada à fila de prioridades!", "#2ecc71");
                limparCampos();
            } else {
                exibirMensagem(resultado, "#e74c3c");
            }

        } catch (NumberFormatException e) {
            exibirMensagem("Duração e Score Clínico devem ser números inteiros.", "#e74c3c");
        } catch (Exception e) {
            exibirMensagem("Erro inesperado: " + e.getMessage(), "#e74c3c");
        }
    }

    private void limparCampos() {
        nomeInput.clear();
        pacienteEmailInput.clear();
        especialidadeInput.getSelectionModel().clearSelection();
        prioridadeInput.getSelectionModel().clearSelection();
        duracaoInput.clear();
        prioridadeClinicaInput.clear();
        tempoRecuperacaoInput.clear();
        dataLimiteInput.setValue(null);
        utiInput.setSelected(false);
    }

    private void exibirMensagem(String texto, String corHex) {
        mensagemFeedback.setText(texto);
        mensagemFeedback.setStyle("-fx-text-fill: " + corHex + "; -fx-font-weight: bold;");
    }
}