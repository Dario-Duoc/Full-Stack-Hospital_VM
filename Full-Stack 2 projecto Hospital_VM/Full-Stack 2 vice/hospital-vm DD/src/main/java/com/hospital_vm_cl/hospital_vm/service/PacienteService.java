package com.hospital_vm_cl.hospital_vm.service;

import com.hospital_vm_cl.hospital_vm.client.ExternoClient;
import com.hospital_vm_cl.hospital_vm.model.Medico;
import com.hospital_vm_cl.hospital_vm.model.Paciente;
import com.hospital_vm_cl.hospital_vm.dto.PacienteDTO;
import com.hospital_vm_cl.hospital_vm.repository.MedicoRepository;
import com.hospital_vm_cl.hospital_vm.repository.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service
public class PacienteService {
    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

    @Autowired
    private PacienteRepository repository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private ExternoClient externoClient;

    public List<Paciente> listarTodos() {
        logger.info("Consultando lista completa de pacientes.");
        return repository.findAll();
    }

    public Paciente guardar(PacienteDTO dto) {
        try {
            logger.info("Iniciando validación remota para paciente con RUN: {}", dto.getRun());
            externoClient.obtenerValidacionExterna(1L); 
            logger.info("Validación externa completada satisfactoriamente.");
        } catch (Exception e) {
            logger.error("Error en comunicación remota: {}. Procediendo localmente.", e.getMessage());
        }

        Paciente paciente = new Paciente();
        paciente.setRun(dto.getRun());
        paciente.setNombres(dto.getNombres());
        paciente.setApellidos(dto.getApellidos());
        paciente.setCorreo(dto.getCorreo());
        paciente.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));

        if (dto.getMedicoId() != null) {
            medicoRepository.findById(dto.getMedicoId()).ifPresent(paciente::setMedico);
        }

        logger.info("Guardando paciente vinculado al médico: {}", 
                    paciente.getMedico() != null ? paciente.getMedico().getNombre() : "Sin asignar");

        return repository.save(paciente);
    }

    public Optional<Paciente> buscarPorId(Long id) {
        logger.info("Buscando paciente con ID: {}", id);
        return repository.findById(id);
    }

    public Paciente actualizar(Long id, PacienteDTO dto) {
        return repository.findById(id).map(p -> {
            p.setRun(dto.getRun());
            p.setNombres(dto.getNombres());
            p.setApellidos(dto.getApellidos());
            p.setCorreo(dto.getCorreo());
            p.setFechaNacimiento(LocalDate.parse(dto.getFechaNacimiento()));
            
            if (dto.getMedicoId() != null) {
                medicoRepository.findById(dto.getMedicoId()).ifPresent(p::setMedico);
            }

            logger.info("Actualizando datos del paciente ID: {}", id);
            return repository.save(p);
        }).orElse(null);
    }

    public boolean eliminar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            logger.warn("Paciente con ID {} eliminado.", id);
            return true;
        }
        return false;
    }
}