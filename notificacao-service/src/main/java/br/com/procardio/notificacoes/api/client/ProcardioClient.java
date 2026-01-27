package br.com.procardio.notificacoes.api.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.procardio.notificacoes.api.dto.UsuarioDTO;

@FeignClient(name = "procardio-api", url = "http://localhost:8080")
public interface ProcardioClient {
    
    @GetMapping("/api/usuarios")
    List<UsuarioDTO> listarUsuarios();

}
