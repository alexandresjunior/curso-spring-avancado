package br.com.procardio.api.dto;

import br.com.procardio.api.enums.Especialidade;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record DadosCadastroMedico(
        Long id,
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String telefone,
        @NotBlank @Pattern(regexp = "\\d{4,6}", message = "CRM deve ter de 4 a 6 digitos numéricos") String crm,
        @NotNull Especialidade especialidade) {
}
