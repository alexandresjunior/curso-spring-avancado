package br.com.procardio.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.procardio.api.dto.LoginDTO;
import br.com.procardio.api.dto.LoginResponseDTO;
import br.com.procardio.api.dto.VerificacaoTfaDTO;
import br.com.procardio.api.model.Usuario;
import br.com.procardio.api.service.TfaService;
import br.com.procardio.api.service.TokenService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TfaService tfaService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> efetuarLogin(@RequestBody @Valid LoginDTO dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        var usuario = (Usuario) authentication.getPrincipal();

        if (usuario.isTfaEnabled()) {
            return ResponseEntity.ok(LoginResponseDTO.aguardandoTfa());
        }

        var tokenJWT = tokenService.gerarToken(usuario);
        return ResponseEntity.ok(LoginResponseDTO.comToken(tokenJWT));
    }

    @PostMapping("/verificar-2fa")
    public ResponseEntity<LoginResponseDTO> verificarTfa(@RequestBody @Valid VerificacaoTfaDTO dados) {
        // 1. Valida usuario e senha novamente (basic auth logic)
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());
        var authentication = manager.authenticate(authenticationToken);
        var usuario = (Usuario) authentication.getPrincipal();

        // 2. Valida o código TOTP
        if (tfaService.codigoValido(usuario.getTfaSecret(), dados.codigo())) {
            var tokenJWT = tokenService.gerarToken(usuario);
            return ResponseEntity.ok(LoginResponseDTO.comToken(tokenJWT));
        }

        return ResponseEntity.badRequest().body(new LoginResponseDTO(null, true, "Código inválido"));
    }

}