package br.com.procardio.notificacoes.api.dto;

public record UsuarioDTO(
    Long id,
    String nome,
    String email,
    EnderecoDTO endereco
) {
    
}
