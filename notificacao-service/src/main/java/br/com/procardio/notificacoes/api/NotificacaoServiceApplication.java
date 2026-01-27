package br.com.procardio.notificacoes.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class NotificacaoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificacaoServiceApplication.class, args);
	}

}
