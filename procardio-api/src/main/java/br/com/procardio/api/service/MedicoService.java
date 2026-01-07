package br.com.procardio.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.procardio.api.enums.Especialidade;
import br.com.procardio.api.model.Medico;
import br.com.procardio.api.repository.MedicoRepository;

@Service
public class MedicoService {
    
    @Autowired
    private MedicoRepository medicoRepository;

    public Medico salvarMedico(Medico medico) {
        return medicoRepository.save(medico);
    }

    public List<Medico> listarMedicos() {
        return medicoRepository.findAll();
    }

    public Medico buscarMedicoPorId(Long id) {
        return medicoRepository.findById(id).orElse(null);
    }

    public void deletarMedico(Long id) {
        medicoRepository.deleteById(id);
    }

    public List<Medico> buscarMedicosPorNome(String nome) {
        return medicoRepository.findByNomeContainingIgnoreCase(nome);
    }

    public Medico buscarMedicoPorEmail(String email) {
        return medicoRepository.findByEmail(email).orElse(null);
    }

    public Medico buscarMedicoPorCrm(String crm) {
        return medicoRepository.findByCrm(crm).orElse(null);
    }

    public List<Medico> buscarMedicosPorEspecialidade(Especialidade especialidade) {
        return medicoRepository.findByEspecialidade(especialidade);
    }

}
