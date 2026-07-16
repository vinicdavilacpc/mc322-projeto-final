package com.agendajava.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.agendajava.backend.model.Manager;
import com.agendajava.backend.model.users.Doctor;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConsultaController {

    @FXML private TextField nomeInput;
    @FXML private DatePicker dataInput;
    @FXML private TextField horaInput;
    @FXML private TextField duracaoInput;
    @FXML private TextField medicoEmailInput;
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
            String emailMedico = medicoEmailInput.getText();

            if (nome.isEmpty() || data == null || horaStr.isEmpty() || duracaoStr.isEmpty() || emailMedico.isEmpty()) {
                exibirMensagem("Preencha todos os campos!", "red");
                return;
            }

            LocalTime hora = LocalTime.parse(horaStr);
            LocalDateTime startDateTime = LocalDateTime.of(data, hora);

            Duration duration = Duration.ofMinutes(Long.parseLong(duracaoStr));

            Doctor doctor = manager.getDoctorByEmail(emailMedico);
            if (doctor == null) {
                exibirMensagem("Médico não encontrado no sistema.", "red");
                return;
            }

            String resultado = manager.appointmentScheduled(nome, startDateTime, duration, doctor);

            if (resultado.equals("Appointment scheduled")) {
                exibirMensagem("Consulta agendada com sucesso!", "green");
            } else {
                exibirMensagem(resultado, "red");
            }

        } catch (DateTimeParseException e) {
            exibirMensagem("Formato de hora inválido. Use HH:mm (ex: 14:30)", "red");
        } catch (NumberFormatException e) {
            exibirMensagem("A duração deve ser um número inteiro (em minutos).", "red");
        } catch (Exception e) {
            exibirMensagem("Erro inesperado: " + e.getMessage(), "red");
        }
    }

    private void exibirMensagem(String texto, String cor) {
        mensagemFeedback.setText(texto);
        mensagemFeedback.setStyle("-fx-text-fill: " + cor + ";");
    }
}