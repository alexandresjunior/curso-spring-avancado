package br.com.procardio.notificacoes.api.consumer;

import java.util.Objects;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.procardio.notificacoes.api.client.GeminiClient;
import br.com.procardio.notificacoes.api.client.ProcardioClient;
import br.com.procardio.notificacoes.api.config.NotificacoesAMQPConfiguration;
import br.com.procardio.notificacoes.api.dto.UsuarioDTO;
import br.com.procardio.notificacoes.api.dto.ConsultaAgendadaEvent;
import br.com.procardio.notificacoes.api.service.EmailService;

@Component
public class NotificacaoConsumer {

    @Autowired
    private ProcardioClient procardioClient;

    @Autowired
    private GeminiClient geminiClient;

    @Autowired
    private EmailService emailService;

    /**
     * Ouve a fila de e-mails. Se lançar exceção não tratada, a mensagem vai para a DLQ
     * configurada em NotificacoesAMQPConfiguration.
     */
    @RabbitListener(queues = NotificacoesAMQPConfiguration.FILA_EMAIL)
    public void receberEventoConsultaAgendada(@Payload ConsultaAgendadaEvent evento) {
        System.out.println("Evento recebido: " + evento);

        // 1. Enriquecimento de Dados: Buscar endereço do paciente
        // (Simulação: filtrando da lista pois a API não tem endpoint por ID)
        UsuarioDTO usuario = buscarDetalhesUsuario(evento.idPaciente());

        if (Objects.nonNull(usuario) && Objects.nonNull(usuario.endereco())) {
            
            // 2. Integração com IA: Gerar dica baseada na localização
            String dicaSaude = geminiClient.gerarMensagemCorpoEmail(usuario.endereco());

            // 3. Ação Final: Enviar E-mail
            String corpoEmail = String.format("""
                    Olá %s! Sua consulta com Dr(a). %s (%s) foi confirmada para %s.
                    
                    Dica do dia para %s:
                    %s
                    """, 
                    evento.nomePaciente(),
                    evento.nomeMedico(),
                    evento.especialidadeMedico(),
                    evento.dataHora(),
                    usuario.endereco().cidade(),
                    dicaSaude
            );

            emailService.enviarEmailPersonalizado(evento.emailPaciente(), evento.nomePaciente(), corpoEmail);
            
            System.out.println("E-mail enviado com sucesso para: " + evento.emailPaciente());
        }
    }

    // Método auxiliar para contornar a falta de endpoint GET /usuarios/{id}
    private UsuarioDTO buscarDetalhesUsuario(Long idPaciente) {
        try {
            return procardioClient.listarUsuarios().stream()
                    .filter(u -> u.id().equals(idPaciente))
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            System.err.println("Erro ao buscar usuário na Procardio API: " + e.getMessage());
            // Se falhar a comunicação, lançamos erro para o RabbitMQ tentar novamente ou mandar pra DLQ
            throw new RuntimeException("Falha na comunicação com Procardio API", e);
        }
    }
    
}