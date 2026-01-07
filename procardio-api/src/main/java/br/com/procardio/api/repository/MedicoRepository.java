package br.com.procardio.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.procardio.api.enums.Especialidade;
import br.com.procardio.api.model.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByEmail(String email);

    Optional<Medico> findByCrm(String crm);

    List<Medico> findByNomeContainingIgnoreCase(String nome);

    List<Medico> findByEspecialidade(Especialidade especialidade);

}
