package br.com.procardio.api.dto;

import br.com.procardio.api.enums.Perfil;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
    @NotBlank
    String nome, 
    @NotBlank
    String email,
    @NotBlank
    String senha,
    String cep,
    String numero,
    String complemento,
    Perfil perfil
) {
}
