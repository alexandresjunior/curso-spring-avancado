package br.com.procardio.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VerificacaoTfaDTO(
    @NotBlank String email,
    @NotBlank String senha,
    @NotNull String codigo
) {}