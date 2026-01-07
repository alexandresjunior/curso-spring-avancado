package br.com.procardio.api.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.procardio.api.model.Consulta;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findByMedico_Id(Long medicoId);

    Optional<Consulta> findByMedico_IdAndDataHora(Long medicoId, LocalDateTime dataHora);

    List<Consulta> findByMedico_IdAndDataHoraBetween(Long medicoId, LocalDateTime dataInicio, LocalDateTime dataFim);

    List<Consulta> findByPaciente_Id(Long pacienteId);

    List<Consulta> findByPaciente_IdAndDataHoraBetween(Long pacienteId, LocalDateTime dataInicio, LocalDateTime dataFim);

}
