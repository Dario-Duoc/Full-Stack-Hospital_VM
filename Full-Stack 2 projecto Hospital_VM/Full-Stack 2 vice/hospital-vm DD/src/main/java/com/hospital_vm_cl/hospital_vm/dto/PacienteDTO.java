package com.hospital_vm_cl.hospital_vm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PacienteDTO {

    @NotBlank(message = "El RUN es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9Kk]{1}$", message = "Formato de RUN inválido (ej: 12345678-9)")
    private String run;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidos;

    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    private String fechaNacimiento;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo electrónico válido")
    private String correo;

    private Long medicoId;
}