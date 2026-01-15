package br.com.procardio.api.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
    @NotBlank
    String email,
    @NotBlank
    String senha
) {
    
}
