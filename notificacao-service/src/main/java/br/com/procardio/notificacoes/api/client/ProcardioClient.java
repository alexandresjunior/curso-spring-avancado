package br.com.procardio.notificacoes.api.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.procardio.notificacoes.api.dto.UsuarioDTO;

@FeignClient(name = "procardio-api", url = "http://localhost:8080")
public interface ProcardioClient {
    
    @GetMapping("/api/usuarios")
    List<UsuarioDTO> listarUsuarios();

    @GetMapping("/api/usuarios/{id}")
    UsuarioDTO buscarUsuarioPorId(@PathVariable Long id);

}
