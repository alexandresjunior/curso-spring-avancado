package br.com.procardio.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.procardio.api.dto.ConsultaAgendadaEvent;
import br.com.procardio.api.exceptions.ConflitoAgendamentoException;
import br.com.procardio.api.model.Consulta;
import br.com.procardio.api.repository.ConsultaRepository;

@Service
public class ConsultaService {
    
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange.eventos}")
    private String exchangeEventos;

    public Consulta salvarConsulta(Consulta consulta) {
        Optional<Consulta> consultaExistente = consultaRepository.findByMedico_IdAndDataHora(consulta.getMedico().getId(), consulta.getDataHora());

        if (consultaExistente.isPresent()) {
            throw new ConflitoAgendamentoException("Conflito de agendamento: o médico já possui uma consulta marcada nesta data para esse horário.");
        }

        Consulta consultaAgendada = consultaRepository.save(consulta);

        ConsultaAgendadaEvent evento = new ConsultaAgendadaEvent(
            consultaAgendada.getId(), 
            consulta.getPaciente().getId(), 
            consulta.getPaciente().getNome(), 
            consulta.getPaciente().getEmail(), 
            consulta.getMedico().getNome(), 
            consulta.getMedico().getEspecialidade().toString(), 
            consulta.getDataHora()
        );

        rabbitTemplate.convertAndSend(exchangeEventos, "consulta.agendada", evento);
        
        return consultaAgendada;
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
