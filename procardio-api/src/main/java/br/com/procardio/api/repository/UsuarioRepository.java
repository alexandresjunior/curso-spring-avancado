package br.com.procardio.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.procardio.api.model.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmailIgnoreCase(String email);

}
