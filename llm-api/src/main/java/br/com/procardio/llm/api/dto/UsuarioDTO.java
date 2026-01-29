package br.com.procardio.llm.api.dto;

public record UsuarioDTO(
    Long id,
    String nome,
    String email,
    EnderecoDTO endereco
) {
    
}
