package br.com.procardio.llm.api.controller;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.procardio.llm.api.dto.EnderecoDTO;
import br.com.procardio.llm.api.service.GeminiService;

@RestController
@RequestMapping("/api/gemini")
public class GeminiController {
    
    @Autowired
    private GeminiService geminiService;

    @PostMapping
    public ResponseEntity<String> gerarMensagemUsuario(@RequestBody EnderecoDTO endereco) {
        if (Objects.isNull(endereco)) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Campo 'endereco' não informado!");
        }

        String mensagem = geminiService.gerarDicaDeSaude(endereco.cidade(), endereco.estado());

        return ResponseEntity.ok(mensagem);
    }

}
