package br.com.procardio.api.dto;

import java.time.LocalDateTime;

/**
 * Evento que representa o fato imutável: "Uma consulta foi agendada".
 * Contém apenas os dados necessários para os consumidores reagirem.
 */
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