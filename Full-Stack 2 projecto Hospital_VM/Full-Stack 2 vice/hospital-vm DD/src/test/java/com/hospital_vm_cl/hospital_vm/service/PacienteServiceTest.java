package com.hospital_vm_cl.hospital_vm.service;

import com.hospital_vm_cl.hospital_vm.client.ExternoClient;
import com.hospital_vm_cl.hospital_vm.dto.PacienteDTO;
import com.hospital_vm_cl.hospital_vm.model.Medico;
import com.hospital_vm_cl.hospital_vm.model.Paciente;
import com.hospital_vm_cl.hospital_vm.repository.MedicoRepository;
import com.hospital_vm_cl.hospital_vm.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private ExternoClient externoClient;

    @InjectMocks
    private PacienteService pacienteService;

    private PacienteDTO pacienteDTO;
    private Medico medicoSimulado;

    @BeforeEach
    public void setup() {
        pacienteDTO = new PacienteDTO();
        pacienteDTO.setRun("19876543-2");
        pacienteDTO.setNombres("Esteban");
        pacienteDTO.setApellidos("Araya");
        pacienteDTO.setCorreo("esteban@gmail.com");
        pacienteDTO.setFechaNacimiento("1995-08-24");
        pacienteDTO.setEspecialidad("Traumatología");
        pacienteDTO.setComentario("Control rutinario");
        pacienteDTO.setMedicoId(1L);

        medicoSimulado = new Medico();
        medicoSimulado.setId(1L);
        medicoSimulado.setNombre("Dr. Camilo Riquelme");
    }
        
    @Test
    @DisplayName("Debería guardar un paciente vinculando su médico si el ID existe")
    public void guardarPacienteExitoso() {
        when(medicoRepository.findById(1L)).thenReturn(Optional.of(medicoSimulado));
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Paciente resultado = pacienteService.guardar(pacienteDTO);

        assertNotNull(resultado);
        assertEquals("19876543-2", resultado.getRun());
        assertEquals("Esteban", resultado.getNombres());
        assertNotNull(resultado.getMedico());
        assertEquals("Dr. Camilo Riquelme", resultado.getMedico().getNombre());

        verify(medicoRepository, times(1)).findById(1L);
        verify(pacienteRepository, times(1)).save(any(Paciente.class));
    }

    @Test
    @DisplayName("Debería retornar un Optional vacío si se busca un ID de paciente nulo")
    public void buscarPorIdNulo() {
        Optional<Paciente> resultado = pacienteService.buscarPorId(null);
        assertTrue(resultado.isEmpty());
        verifyNoInteractions(pacienteRepository);
    }
}