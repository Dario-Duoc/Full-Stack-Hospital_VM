package com.hospital_vm_cl.hospital_vm.service;

import com.hospital_vm_cl.hospital_vm.model.Medico;
import com.hospital_vm_cl.hospital_vm.repository.MedicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MedicoService {

    private static final Logger logger = LoggerFactory.getLogger(MedicoService.class);

    @Autowired
    private MedicoRepository repository;

    public List<Medico> listarTodos() {
        logger.info("Iniciando consulta de la lista completa de médicos");
        return repository.findAll();
    }

    public Medico guardar(Medico medico) {
        try {
            logger.info("Intentando registrar médico: {}", medico.getNombre());
            return repository.save(medico);
        } catch (Exception e) {
            logger.error("Error al guardar el médico: {}", e.getMessage());
            throw e;
        }
    }
}