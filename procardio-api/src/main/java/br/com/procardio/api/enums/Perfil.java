package br.com.procardio.api.enums;

public enum Perfil {
    ADMIN("ROLE_ADMIN"),
    MEDICO("ROLE_MEDICO"),
    PACIENTE("ROLE_PACIENTE");

    private String role;

    Perfil(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
