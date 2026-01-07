package br.com.procardio.api.model;

import java.time.LocalDateTime;

import br.com.procardio.api.dto.DadosAgendamentoConsulta;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medico medico;

    private LocalDateTime data;

    @Deprecated
    public Consulta() {
    }

    public Consulta(Medico medico, DadosAgendamentoConsulta dados) {
        modificarDados(medico, dados);
    }

    public void modificarDados(Medico medico, DadosAgendamentoConsulta dados) {
        this.medico = medico;
        this.paciente = dados.paciente();
        this.data = dados.data();
    }

}
