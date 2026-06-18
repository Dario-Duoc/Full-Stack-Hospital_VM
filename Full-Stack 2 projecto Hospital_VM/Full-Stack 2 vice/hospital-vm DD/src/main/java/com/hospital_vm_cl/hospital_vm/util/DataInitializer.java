package com.hospital_vm_cl.hospital_vm.util;

import com.hospital_vm_cl.hospital_vm.model.Medico;
import com.hospital_vm_cl.hospital_vm.model.Paciente;
import com.hospital_vm_cl.hospital_vm.repository.MedicoRepository;
import com.hospital_vm_cl.hospital_vm.repository.PacienteRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;
    
    private final Faker faker = new Faker(Locale.forLanguageTag("es"));
    private final Random random = new Random();

    public DataInitializer(MedicoRepository medicoRepository, PacienteRepository pacienteRepository) {
        this.medicoRepository = medicoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // POBLAR MÉDICOS (Solo funciona si la TABLA está vacía)
        List<Medico> medicosGuardados = new ArrayList<>();
        
        if (medicoRepository.count() == 0) {
            System.out.println(">> Base de datos vacía. Generando médicos con Datafaker...");
            
            String[] especialidades = {
                "Traumatología", "Neurocirugía", "Cardiología", 
                "Pediatría", "Medicina General", "Urgencias"
            };

            for (int i = 0; i < 10; i++) {
                Medico medico = new Medico();
                medico.setNombre("Dr. " + faker.name().firstName() + " " + faker.name().lastName());
                medico.setRut(faker.number().digits(8) + "-" + faker.number().digits(1));
                medico.setEspecialidad(faker.options().option(especialidades)); 
                
                medico = medicoRepository.save(medico); 
                medicosGuardados.add(medico);
            }
            System.out.println(">> ¡10 Médicos creados con éxito! <<");
        } else {
            medicosGuardados = medicoRepository.findAll();
        }

        // POBLAR 500 PACIENTES
        if (pacienteRepository.count() == 0 && !medicosGuardados.isEmpty()) {
            System.out.println(">> Generando 500 pacientes de prueba vinculados a los médicos...");

            for (int i = 0; i < 500; i++) {
                Paciente paciente = new Paciente();
                paciente.setRun(faker.number().digits(8) + "-" + faker.number().digits(1));
                paciente.setNombres(faker.name().firstName());
                paciente.setApellidos(faker.name().lastName() + " " + faker.name().lastName());
                paciente.setCorreo(faker.internet().emailAddress());
                
                paciente.setFechaNacimiento(LocalDate.parse(faker.date().birthday(1, 90, "yyyy-MM-dd")));                
                paciente.setComentario(faker.options().option(
                    "Control rutinario", "Presenta dolor agudo", "Post-operatorio", "Revisión de exámenes"
                ));

                // Selecciona un médico aleatorio
                Medico medicoAleatorio = medicosGuardados.get(random.nextInt(medicosGuardados.size()));
                paciente.setMedico(medicoAleatorio);
                paciente.setEspecialidad(medicoAleatorio.getEspecialidad());

                pacienteRepository.save(paciente);
            }
            System.out.println(">> ¡500 Pacientes generados y asociados exitosamente.! <<");
        }
    }
}