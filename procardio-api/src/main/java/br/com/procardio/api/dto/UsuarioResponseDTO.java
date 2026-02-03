package br.com.procardio.api.dto;

public record UsuarioResponseDTO(
        Long id,
        String nome,
        String email,
        EnderecoDTO endereco
) {

}
