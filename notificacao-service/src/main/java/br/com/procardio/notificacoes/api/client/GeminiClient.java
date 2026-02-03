package br.com.procardio.notificacoes.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.procardio.notificacoes.api.dto.EnderecoDTO;

@FeignClient(name = "llm-api", url = "http://localhost:8082")
public interface GeminiClient {
    
    @PostMapping("/api/gemini")
    String gerarMensagemCorpoEmail(EnderecoDTO enderecoDTO);

}
