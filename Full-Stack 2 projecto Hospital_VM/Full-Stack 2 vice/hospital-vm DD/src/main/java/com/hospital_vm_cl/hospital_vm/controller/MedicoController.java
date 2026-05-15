package com.hospital_vm_cl.hospital_vm.controller;

import com.hospital_vm_cl.hospital_vm.model.Medico;
import com.hospital_vm_cl.hospital_vm.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService service;

    @GetMapping
    public ResponseEntity<List<Medico>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Medico> crear(@Valid @RequestBody Medico medico) {
        return new ResponseEntity<>(service.guardar(medico), HttpStatus.CREATED);
    }
}