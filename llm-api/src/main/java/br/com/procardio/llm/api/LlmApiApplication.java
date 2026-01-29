package br.com.procardio.llm.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class LlmApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlmApiApplication.class, args);
	}

}
