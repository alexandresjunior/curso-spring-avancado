package br.com.procardio.api.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.procardio.api.dto.UsuarioDTO;
import br.com.procardio.api.dto.UsuarioResponseDTO;
import br.com.procardio.api.model.Usuario;
import br.com.procardio.api.service.TfaService;
import br.com.procardio.api.service.UsuarioService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TfaService tfaService;

    // 1. Iniciar configuração: Gera Segredo e retorna QR Code
    @PostMapping("/{id}/2fa/setup")
    public ResponseEntity<byte[]> setup2fa(@PathVariable Long id) throws QrGenerationException {
        var usuario = usuarioService.buscarUsuarioPorId(id);

        String secret = tfaService.gerarNovoSegredo();

        usuario.setTfaSecret(secret);
        usuario.setTfaEnabled(false); // Ainda não ativa, espera confirmação

        usuarioService.salvarUsuario(usuario);

        byte[] imagem = tfaService.gerarQrCodeImagem(secret, usuario.getEmail());

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imagem);
    }

    // 2. Confirmar configuração: Recebe código para validar e ativar
    @PostMapping("/{id}/2fa/confirmar")
    public ResponseEntity<String> confirmar2fa(@PathVariable Long id, @RequestBody String codigo) {
        var usuario = usuarioService.buscarUsuarioPorId(id);

        if (tfaService.codigoValido(usuario.getTfaSecret(), codigo)) {
            usuario.setTfaEnabled(true);

            usuarioService.salvarUsuario(usuario);

            return ResponseEntity.ok("Autenticação de dois fatores ativada com sucesso!");
        }

        return ResponseEntity.badRequest().body("Código inválido. O 2FA não foi ativado.");
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario novoUsuario = usuarioService.salvarUsuario(usuarioDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(novoUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@PathVariable Long id,
            @Valid @RequestBody UsuarioDTO usuarioDTO) {
        Usuario usuarioAtualizado = usuarioService.salvarUsuario(id, usuarioDTO);

        if (Objects.nonNull(usuarioAtualizado)) {
            return ResponseEntity.ok(new UsuarioResponseDTO(usuarioAtualizado));
        }
        return ResponseEntity.notFound().build();
    }

}
