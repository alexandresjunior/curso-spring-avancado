package br.com.procardio.notificacoes.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.procardio.notificacoes.api.client.ProcardioClient;
import br.com.procardio.notificacoes.api.dto.UsuarioDTO;

@RestController
@RequestMapping("/api/notificacoes")
public class NotificacaoController {
    
    @Autowired
    private ProcardioClient procardioClient;

    @PostMapping("/boas-vindas")
    public ResponseEntity<String> enviarEmailBoasVindas() {
        List<UsuarioDTO> usuarios = procardioClient.listarUsuarios();

        if (usuarios.isEmpty()) {
            return ResponseEntity.ok("Nenhum usuario encontrado na Procardio API.");
        }

        StringBuilder logEnvio = new StringBuilder();

        for (UsuarioDTO usuario : usuarios) {
            logEnvio.append("Enviando e-mail para: ")
                    .append(usuario.email())
                    .append("\n");
        }

        return ResponseEntity.ok(logEnvio.toString());
    } 


}
