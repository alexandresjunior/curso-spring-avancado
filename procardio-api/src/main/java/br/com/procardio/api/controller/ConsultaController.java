package br.com.procardio.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.procardio.api.model.Consulta;
import br.com.procardio.api.service.ConsultaService;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    /**
     * TODO: Endpoints de Consulta
     * 1. CRUD
     * 2. Filtro por medico, paciente e data_hora
     * 3. Permissoes:
     *      - Cadastrar/Atualizar: ADMIN, PACIENTE
     *      - Buscar: ADMIN, PACIENTE, MEDICO
     *      - Excluir: ADMIN
     */

    @Autowired
    private ConsultaService consultaService;

    @PostMapping
    public ResponseEntity<Consulta> agendarConsulta(@RequestBody Consulta consulta) {
        try {
            Consulta consultaAgendada = consultaService.salvarConsulta(consulta);
            return ResponseEntity.ok(consultaAgendada);
        } catch (Exception e) {
            System.err.println("Erro ao agendar consulta: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consulta> buscarConsultaPorId(@PathVariable Long id) {
        Consulta consulta = consultaService.buscarConsultaPorId(id);
        if (consulta != null) {
            return ResponseEntity.ok(consulta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
