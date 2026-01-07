package br.com.procardio.api.dto;

import java.time.LocalDateTime;

import br.com.procardio.api.enums.Especialidade;
import br.com.procardio.api.model.Consulta;

public record DadosListagemConsulta(
        Long id,
        String medico,
        String paciente,
        LocalDateTime data,
        Especialidade especialidade) {

    public DadosListagemConsulta(Consulta consulta) {
        this(consulta.getId(),
                consulta.getMedico().getNome(),
                consulta.getPaciente(),
                consulta.getData(),
                consulta.getMedico().getEspecialidade());
    }

}
