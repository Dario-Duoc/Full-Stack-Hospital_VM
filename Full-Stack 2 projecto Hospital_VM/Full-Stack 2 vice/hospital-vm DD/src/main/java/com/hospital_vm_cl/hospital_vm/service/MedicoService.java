package com.hospital_vm_cl.hospital_vm.service;

import com.hospital_vm_cl.hospital_vm.model.Medico;
import com.hospital_vm_cl.hospital_vm.repository.MedicoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {
    private static final Logger logger = LoggerFactory.getLogger(MedicoService.class);

    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public List<Medico> listarTodos() {
        logger.info("Consultando lista completa de médicos.");
        return medicoRepository.findAll();
    }

    public Optional<Medico> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        logger.info("Buscando médico con ID: {}", id);
        return medicoRepository.findById(id);
    }

    public Medico guardar(Medico medico) {
        logger.info("Registrando nuevo médico: {}", medico.getNombre());
        return medicoRepository.save(medico);
    }

    public Medico actualizar(Long id, Medico detalles) {
        if (id == null) {
            return null;
        }
        return medicoRepository.findById(id).map(m -> {
            m.setNombre(detalles.getNombre());
            m.setRut(detalles.getRut());
            m.setEspecialidad(detalles.getEspecialidad());
            logger.info("Actualizando datos del médico ID: {}", id);
            return medicoRepository.save(m);
        }).orElse(null);
    }

    public boolean eliminar(Long id) {
        if (id == null) {
            return false;
        }
        if (medicoRepository.existsById(id)) {
            medicoRepository.deleteById(id);
            logger.warn("Médico con ID {} eliminado de la base de datos.", id);
            return true;
        }
        return false;
    }
}