package br.com.procardio.llm.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    /**
     * NOTA:
     * O RestClient é uma funcionalidade recente (introduzida no Spring Boot 3.2). 
     * Em algumas configurações, o Spring Boot não expõe o Builder como um bean injetável por padrão, 
     * exigindo que ele seja declarado manualmente ou utilize o método estático RestClient.create() diretamente. 
     * Com essa classe, é garantido que o Spring tenha um Builder disponível para ser injetado no GeminiService.
     */

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

}