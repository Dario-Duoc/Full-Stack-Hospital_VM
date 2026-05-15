package com.hospital_vm_cl.hospital_vm.controller;

import com.hospital_vm_cl.hospital_vm.model.Paciente;
import com.hospital_vm_cl.hospital_vm.dto.PacienteDTO;
import com.hospital_vm_cl.hospital_vm.service.PacienteService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*")
public class PacienteController {

    private static final Logger logger = LoggerFactory.getLogger(PacienteController.class);

    @Autowired
    private PacienteService service;

    @GetMapping
    public ResponseEntity<List<Paciente>> getAllPacientes() {
        logger.info("Consultando lista completa de pacientes.");
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Paciente> createPaciente(@Valid @RequestBody PacienteDTO pacienteDto) {
        logger.info("Recibida solicitud para crear paciente con correo: {}", pacienteDto.getCorreo());
        Paciente nuevo = service.guardar(pacienteDto); 
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> getPacienteById(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> updatePaciente(@PathVariable Long id, @Valid @RequestBody PacienteDTO pacienteDto) {
        logger.info("Actualizando datos del paciente ID: {}", id);
        Paciente actualizado = service.actualizar(id, pacienteDto);
        return (actualizado != null) ? ResponseEntity.ok(actualizado) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaciente(@PathVariable Long id) {
        logger.warn("Solicitud de eliminación para paciente ID: {}", id);
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}