package br.com.procardio.api.exceptions;

public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado com ID: " + id);
    }

    public UsuarioNaoEncontradoException(String email) {
        super("Usuário não encontrado com e-mail: " + email);
    }
    
}
