package br.com.procardio.notificacoes.api.scheduler;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.procardio.notificacoes.api.client.GeminiClient;
import br.com.procardio.notificacoes.api.client.ProcardioClient;
import br.com.procardio.notificacoes.api.dto.EnderecoDTO;
import br.com.procardio.notificacoes.api.dto.UsuarioDTO;
import br.com.procardio.notificacoes.api.service.EmailService;

@Component
public class NotificacaoScheduler {
    
    @Autowired
    private ProcardioClient procardioClient;

    @Autowired
    private GeminiClient geminiClient;

    @Autowired
    private EmailService emailService;

    @Scheduled(cron = "0 0 8 * * 1")
    public void processarEnvioEmails() {
        System.out.println("----- Iniciando rotina de envio de e-mails -----");

        try {
            // Chamada ao web service 'procardio-api' para obter lista de usuarios
            List<UsuarioDTO> usuarios = procardioClient.listarUsuarios();

            for (UsuarioDTO usuario : usuarios) {
                String cidade = usuario.endereco().cidade();
                String estado = usuario.endereco().estado();

                if (Objects.nonNull(cidade) && Objects.nonNull(estado)) {
                    // Chamada ao web service 'llm-api' para gerar mensagem personalizada
                    String mensagem = geminiClient.gerarMensagemCorpoEmail(new EnderecoDTO(cidade, estado));

                    // Envio do email para o usuario
                    emailService.enviarEmailPersonalizado(cidade, estado, mensagem);
                }
            }
        } catch (Exception ex) {
            System.err.println("Falha na execucao do job: " + ex.getMessage());
        }

        System.out.println("----- Rotina finalizada! -----");
    }

}
