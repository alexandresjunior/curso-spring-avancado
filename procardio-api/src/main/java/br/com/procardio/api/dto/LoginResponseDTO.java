package br.com.procardio.api.dto;

public record LoginResponseDTO(
    String token, 
    boolean tfaEnabled, 
    String mensagem
) {
    // Construtor para login direto (sem 2FA ou token final)
    public static LoginResponseDTO comToken(String token) {
        return new LoginResponseDTO(token, false, null);
    }

    // Construtor para quando 2FA é necessário
    public static LoginResponseDTO aguardandoTfa() {
        return new LoginResponseDTO(null, true, "Autenticação 2FA necessária. Envie o código TOTP.");
    }
}