package br.com.procardio.llm.api.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.procardio.llm.api.client.ProcardioClient;
import br.com.procardio.llm.api.dto.EnderecoDTO;
import br.com.procardio.llm.api.dto.UsuarioDTO;
import br.com.procardio.llm.api.service.GeminiService;

@RestController
@RequestMapping("/api/gemini")
public class GeminiController {
    
    @Autowired
    private GeminiService geminiService;

    @Autowired
    private ProcardioClient procardioClient;

    @GetMapping("/usuario/{id}")
    public ResponseEntity<String> gerarMensagemUsuario(@PathVariable Long id) {
        UsuarioDTO usuario = procardioClient.obterDadosUsuario(id);

        if (Objects.isNull(usuario)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado!");
        }

        EnderecoDTO endereco = usuario.endereco();

        if (Objects.isNull(endereco)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário sem endereço não cadastrado!");
        }

        String mensagem = geminiService.gerarDicaDeSaude(endereco.cidade(), endereco.estado());

        return ResponseEntity.ok(mensagem);
    }

}
