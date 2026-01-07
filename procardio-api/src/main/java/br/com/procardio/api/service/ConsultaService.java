package br.com.procardio.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.procardio.api.exceptions.ConflitoAgendamentoException;
import br.com.procardio.api.model.Consulta;
import br.com.procardio.api.repository.ConsultaRepository;

@Service
public class ConsultaService {
    
    @Autowired
    private ConsultaRepository consultaRepository;

    public Consulta salvarConsulta(Consulta consulta) {
        Optional<Consulta> consultaExistente = consultaRepository.findByMedico_IdAndDataHora(consulta.getMedico().getId(), consulta.getDataHora());

        if (consultaExistente.isPresent()) {
            throw new ConflitoAgendamentoException("Conflito de agendamento: o médico já possui uma consulta marcada nesta data para esse horário.");
        }
        
        return consultaRepository.save(consulta);
    }

    public List<Consulta> listarConsultas() {
        return consultaRepository.findAll();
    }

    public Consulta buscarConsultaPorId(Long id) {
        return consultaRepository.findById(id).orElse(null);
    }

    public void deletarConsulta(Long id) {
        consultaRepository.deleteById(id);
    }

    public List<Consulta> buscarConsultasPorMedico(Long medicoId) {
        return consultaRepository.findByMedico_Id(medicoId);
    }

    public List<Consulta> buscarConsultasPorPaciente(Long pacienteId) {
        return consultaRepository.findByPaciente_Id(pacienteId);
    }

    public List<Consulta> buscarConsultasPorMedicoEPeriodo(Long medicoId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return consultaRepository.findByMedico_IdAndDataHoraBetween(medicoId, dataInicio, dataFim);
    }

    public List<Consulta> buscarConsultasPorPacienteEPeriodo(Long pacienteId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        return consultaRepository.findByPaciente_IdAndDataHoraBetween(pacienteId, dataInicio, dataFim);
    }

}
