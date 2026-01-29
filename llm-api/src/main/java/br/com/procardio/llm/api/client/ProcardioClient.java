package br.com.procardio.llm.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.procardio.llm.api.dto.UsuarioDTO;

@FeignClient(name = "procardio-api", url="${procardio.api.url}")
public interface ProcardioClient {

    @GetMapping("/api/usuarios/{id}")
    UsuarioDTO obterDadosUsuario(@PathVariable Long id);
    
}
