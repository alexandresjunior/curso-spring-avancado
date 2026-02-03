package br.com.procardio.notificacoes.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${app.mail.remetente}")
    private String remetente;

    public void enviarEmailPersonalizado(String destinario, String nome, String mensagem) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            Context context = new Context();
            context.setVariable("nome", nome);
            context.setVariable("mensagemIa", mensagem);

            String htmlContent = templateEngine.process("email-saude", context);

            helper.setFrom(remetente);
            helper.setTo(destinario);
            helper.setSubject("ProCardio - Sua Dica de Saúde da Semana");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException ex) {
            System.err.println("Erro ao enviar e-mail: " + ex.getMessage());
        }
    }
    
}
