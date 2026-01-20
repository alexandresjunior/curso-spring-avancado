package br.com.procardio.api.dto;

import java.util.Set;

import br.com.procardio.api.enums.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioDTO(
    @NotBlank
    String nome, 
    @NotBlank @Email
    String email,
    @NotBlank
    String senha,
    String cep,
    String numero,
    String complemento,
    Set<Perfil> perfis
) {
}
