package br.com.procardio.notificacoes.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificacoesAMQPConfiguration {
    
    public static final String EXCHANGE_EVENTOS = "procardio.v1.eventos";
    public static final String EXCHANGE_DLX = "procardio.v1.dlx";
    public static final String FILA_EMAIL = "notificacoes.email-consulta";
    public static final String FILA_EMAIL_DLQ = "notificacoes.email-consulta.dlq";  // Dead Letter Queue (DLQ)
    public static final String ROUTING_KEY_CONSULTA = "consulta.agendada";


    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitAdmin criarRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> inicializaAdmin(RabbitAdmin ra) {
        return event -> ra.initialize();
    }

    @Bean
    public Exchange criarExchangeDeadLetter() {
        return ExchangeBuilder.topicExchange(EXCHANGE_DLX)
                .durable(true)
                .build();
    }

    @Bean
    public Queue criarFilaDLQ() {
        return QueueBuilder.nonDurable(FILA_EMAIL_DLQ).build();
    }

    @Bean
    public Binding criarBindingDLQ() {
        return BindingBuilder.bind(criarFilaDLQ())
                .to(criarExchangeDeadLetter())
                .with(FILA_EMAIL_DLQ)
                .noargs();
    }

    @Bean
    public Exchange criarExchangeEventos() {
        return ExchangeBuilder.topicExchange(EXCHANGE_EVENTOS)
                .durable(true)
                .build();
    }

    @Bean
    public Queue criarFila() {
        return QueueBuilder.nonDurable(FILA_EMAIL)
                .withArgument("x-dead-letter-exchange", EXCHANGE_DLX)           // Configura a DLX para esta fila
                .withArgument("x-dead-letter-routing-key", FILA_EMAIL_DLQ)      // Configura a routing key para a DLQ
                .build();
    }

    @Bean
    public Binding criarBinding() {
        return BindingBuilder.bind(criarFila())
                .to(criarExchangeEventos())
                .with(ROUTING_KEY_CONSULTA)
                .noargs();
    }

}
