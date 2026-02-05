package br.com.procardio.api.config;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração da infraestrutura de mensageria.
 * Responsável por definir como as mensagens são serializadas e criar a Exchange.
 */
@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.exchange.eventos}")
    private String exchangeEventos;

    /**
     * Cria a Exchange (ponto de entrada da mensagem no RabbitMQ) automaticamente
     * caso ela não exista. Usamos TOPIC para permitir roteamento por chaves.
     */
    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.topicExchange(exchangeEventos)
                .durable(true)
                .build();
    }

    /**
     * Define que as mensagens serão convertidas para JSON (Jackson) em vez
     * da serialização nativa do Java. Isso garante interoperabilidade.
     */
    @Bean
    public JacksonJsonMessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
    
}