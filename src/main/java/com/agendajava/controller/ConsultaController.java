package com.agendajava.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.agendajava.backend.model.Manager;
import com.agendajava.backend.model.users.Doctor;
import com.agendajava.backend.model.users.Patient;
import com.agendajava.backend.model.users.User;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConsultaController {

    @FXML private TextField nomeInput;
    @FXML private DatePicker dataInput;
    @FXML private TextField horaInput;
    @FXML private TextField duracaoInput;
    @FXML private TextField emailContatoInput;
    @FXML private Label mensagemFeedback;

    private Manager manager;

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    @FXML
    public void agendarConsulta() {
        try {
            String nome = nomeInput.getText();
            LocalDate data = dataInput.getValue();
            String horaStr = horaInput.getText();
            String duracaoStr = duracaoInput.getText();
            String emailContato = emailContatoInput.getText();

            if (nome.isEmpty() || data == null || horaStr.isEmpty() || duracaoStr.isEmpty() || emailContato.isEmpty()) {
                exibirMensagem("Preencha todos os campos!", "#e74c3c");
                return;
            }

            long minutos = Long.parseLong(duracaoStr);
            if (minutos <= 0) {
                exibirMensagem("A duração da consulta deve ser maior que zero.", "#e74c3c");
                return;
            }

            LocalTime hora = LocalTime.parse(horaStr);
            LocalDateTime startDateTime = LocalDateTime.of(data, hora);
            Duration duration = Duration.ofMinutes(minutos);

            User currentUser = manager.getCurrentUser();
            Doctor doctor = null;
            Patient patient = null;

            if (currentUser instanceof Doctor) {
                doctor = (Doctor) currentUser;
                patient = manager.getPatientByEmail(emailContato);
                if (patient == null) {
                    exibirMensagem("Paciente não encontrado no sistema.", "#e74c3c");
                    return;
                }
            } else {
                patient = (Patient) currentUser;
                doctor = manager.getDoctorByEmail(emailContato);
                if (doctor == null) {
                    exibirMensagem("Médico não encontrado no sistema.", "#e74c3c");
                    return;
                }
            }

            String resultado = manager.appointmentScheduled(nome, startDateTime, duration, patient, doctor);

            if (resultado.equals("Appointment scheduled")) {
                exibirMensagem("Consulta agendada com sucesso!", "#2ecc71");
                limparCampos();
            } else {
                exibirMensagem(resultado, "#e74c3c");
            }

        } catch (DateTimeParseException e) {
            exibirMensagem("Formato de hora inválido. Use HH:mm (ex: 14:30)", "#e74c3c");
        } catch (NumberFormatException e) {
            exibirMensagem("A duração deve ser um número inteiro.", "#e74c3c");
        } catch (Exception e) {
            exibirMensagem("Erro inesperado: " + e.getMessage(), "#e74c3c");
        }
    }

    private void limparCampos() {
        nomeInput.clear();
        dataInput.setValue(null);
        horaInput.clear();
        duracaoInput.clear();
        emailContatoInput.clear();
    }

    private void exibirMensagem(String texto, String corHex) {
        mensagemFeedback.setText(texto);
        mensagemFeedback.setStyle("-fx-text-fill: " + corHex + "; -fx-font-weight: bold;");
    }
}