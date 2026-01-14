package br.com.procardio.api.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.procardio.api.dto.UsuarioDTO;
import br.com.procardio.api.dto.UsuarioResponseDTO;
import br.com.procardio.api.model.Usuario;
import br.com.procardio.api.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario novoUsuario = usuarioService.salvarUsuario(usuarioDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(novoUsuario));
    }

    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioAtualizado = usuarioService.salvarUsuario(id, usuarioDTO);
        
        if (Objects.nonNull(usuarioAtualizado)) {
            return ResponseEntity.ok(new UsuarioResponseDTO(usuarioAtualizado));
        }
        return ResponseEntity.notFound().build();
    }

}
