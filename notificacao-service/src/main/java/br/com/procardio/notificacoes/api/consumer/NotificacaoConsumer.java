package br.com.procardio.notificacoes.api.consumer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.procardio.notificacoes.api.client.GeminiClient;
import br.com.procardio.notificacoes.api.client.ProcardioClient;
import br.com.procardio.notificacoes.api.config.NotificacoesAMQPConfiguration;
import br.com.procardio.notificacoes.api.dto.ConsultaAgendadaEvent;
import br.com.procardio.notificacoes.api.dto.UsuarioDTO;
import br.com.procardio.notificacoes.api.service.EmailService;

@Component
public class NotificacaoConsumer {

    @Autowired
    private ProcardioClient procardioClient;

    @Autowired
    private GeminiClient geminiClient;

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = NotificacoesAMQPConfiguration.FILA_EMAIL)
    public void receberEventoConsultaAgendada(@Payload ConsultaAgendadaEvent evento) {
        System.out.println("Recebendo evento de consulta agendada: " + evento);

        UsuarioDTO usuario = procardioClient.buscarUsuarioPorId(evento.idPaciente());

        if (Objects.nonNull(usuario) && Objects.nonNull(usuario.endereco())) {
            String mensagemEmail = geminiClient.gerarMensagemCorpoEmail(usuario.endereco());

            String corpoEmail = String.format("""
                    Olá, %s! Sua consulta com Dr(a). %s foi agendada para o dia %s às %s.

                    Dica do dia:
                    %s
                    """, 
                    evento.nomePaciente(), 
                    evento.nomeMedico(), 
                    extrairDataConsulta(evento.dataHora()), 
                    extrairHoraConsulta(evento.dataHora()), 
                    mensagemEmail);

            emailService.enviarEmailPersonalizado(evento.emailPaciente(), evento.nomePaciente(), corpoEmail);

            System.out.println("Email enviado para " + evento.emailPaciente() + " com sucesso!");
        }
    }    

    private LocalDate extrairDataConsulta(LocalDateTime dataHora) {
        return dataHora.toLocalDate();
    }

    private LocalTime extrairHoraConsulta(LocalDateTime dataHora) {
        return dataHora.toLocalTime();
    }

}
