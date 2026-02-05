package br.com.procardio.notificacoes.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificacoesAMQPConfiguration {

    // Nomes das filas e exchanges (poderiam vir do application.properties)
    public static final String EXCHANGE_EVENTOS = "procardio.v1.events";
    public static final String EXCHANGE_DLX = "procardio.v1.dlx"; // Dead Letter Exchange
    public static final String FILA_EMAIL = "notificacoes.email-consulta";
    public static final String FILA_EMAIL_DLQ = "notificacoes.email-consulta.dlq";
    public static final String ROUTING_KEY_CONSULTA = "consulta.agendada";
    
    /**
     * Converter para JSON: garante que o microsserviço consiga ler o objeto
     * enviado pela procardio-api (que também usa JSON).
     */
    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // =========================================================================
    // CONFIGURAÇÃO DA DEAD LETTER QUEUE (DLQ)
    // =========================================================================
    
    @Bean
    public Exchange deadLetterExchange() {
        // Exchange específica para onde as mensagens com erro serão enviadas
        return ExchangeBuilder.directExchange(EXCHANGE_DLX).durable(true).build();
    }

    @Bean
    public Queue filaEmailDLQ() {
        // A fila de "cemitério" onde ficam as mensagens que falharam após retentativas
        return QueueBuilder.durable(FILA_EMAIL_DLQ).build();
    }

    @Bean
    public Binding bindingDLQ() {
        // Liga a DLX à fila de DLQ
        return BindingBuilder.bind(filaEmailDLQ())
                .to(deadLetterExchange())
                .with(FILA_EMAIL_DLQ) // Routing key usada no redirecionamento
                .noargs();
    }

    // =========================================================================
    // CONFIGURAÇÃO DA FILA PRINCIPAL
    // =========================================================================

    @Bean
    public Exchange exchangeEventos() {
        // A mesma exchange definida na procardio-api
        return ExchangeBuilder.topicExchange(EXCHANGE_EVENTOS).durable(true).build();
    }

    @Bean
    public Queue filaEmail() {
        // Configuração avançada: Se der erro, manda para a DLX
        return QueueBuilder.durable(FILA_EMAIL)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)
                .withArgument("x-dead-letter-routing-key", FILA_EMAIL_DLQ)
                .build();
    }

    @Bean
    public Binding bindingEmail() {
        // Assinamos apenas mensagens com a routing key "consulta.agendada"
        return BindingBuilder.bind(filaEmail())
                .to(exchangeEventos())
                .with(ROUTING_KEY_CONSULTA)
                .noargs();
    }

}
