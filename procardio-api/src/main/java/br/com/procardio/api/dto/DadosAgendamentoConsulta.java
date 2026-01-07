package br.com.procardio.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import br.com.procardio.api.enums.Especialidade;

public record DadosAgendamentoConsulta(
        Long id,
        Long idMedico,

        @NotNull String paciente,

        @NotNull @Future LocalDateTime data,

        Especialidade especialidade) {
}
