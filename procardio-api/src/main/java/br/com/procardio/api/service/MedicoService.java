package br.com.procardio.api.service;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.procardio.api.dto.DadosCadastroMedico;
import br.com.procardio.api.dto.DadosListagemMedico;
import br.com.procardio.api.enums.Especialidade;
import br.com.procardio.api.model.Medico;
import br.com.procardio.api.repository.MedicoRepository;

import java.util.List;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository repository;

    public Page<DadosListagemMedico> listar(Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemMedico::new);
    }

    @Transactional
    public void cadastrar(DadosCadastroMedico dados) {
        if (dados.id() == null) {
            repository.save(new Medico(dados));
        } else {
            var medico = repository.findById(dados.id()).orElseThrow();
            medico.atualizarDados(dados);
        }
    }

    public DadosCadastroMedico carregarPorId(Long id) {
        var medico = repository.findById(id).orElseThrow();
        return new DadosCadastroMedico(medico.getId(), medico.getNome(), medico.getEmail(), medico.getTelefone(),
                medico.getCrm(), medico.getEspecialidade());
    }

    @Transactional
    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public List<DadosListagemMedico> listarPorEspecialidade(Especialidade especialidade) {
        return repository.findByEspecialidade(especialidade).stream().map(DadosListagemMedico::new).toList();
    }

}
