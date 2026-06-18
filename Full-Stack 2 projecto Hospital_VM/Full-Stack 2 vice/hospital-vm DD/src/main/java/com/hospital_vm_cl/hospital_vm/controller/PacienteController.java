package com.hospital_vm_cl.hospital_vm.controller;

import com.hospital_vm_cl.hospital_vm.model.Paciente;
import com.hospital_vm_cl.hospital_vm.dto.PacienteDTO;
import com.hospital_vm_cl.hospital_vm.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // HATEOAS
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Paciente>> obtenerPorId(@PathVariable Long id) {
        return pacienteService.buscarPorId(id)
                .map(paciente -> {
                    EntityModel<Paciente> modelo = EntityModel.of(paciente);
                    
                    Link selfLink = linkTo(methodOn(PacienteController.class).obtenerPorId(id)).withSelfRel();
                    
                    Link todosLink = linkTo(methodOn(PacienteController.class).listarPacientes()).withRel("todos-los-pacientes");
                    
                    modelo.add(selfLink, todosLink);
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Paciente>> listarPacientes() {
        List<Paciente> pacientes = pacienteService.listarTodos();
        return ResponseEntity.ok(pacientes);
    }
    
    @PostMapping
    public ResponseEntity<Paciente> agregarPaciente(@Valid @RequestBody PacienteDTO dto) {
        Paciente nuevoPaciente = pacienteService.guardar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPaciente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Paciente> modificarPaciente(@PathVariable Long id, @Valid @RequestBody PacienteDTO dto) {
        Paciente actualizado = pacienteService.actualizar(id, dto);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        if (pacienteService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}