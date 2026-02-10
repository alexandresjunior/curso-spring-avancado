package br.com.procardio.api.dto;

import java.time.LocalDateTime;

public record ConsultaAgendadaEvent(
    Long idConsulta,
    Long idPaciente,
    String nomePaciente,
    String emailPaciente,
    String nomeMedico,
    String especialidadeMedico,
    LocalDateTime dataHora
) {
    
}
