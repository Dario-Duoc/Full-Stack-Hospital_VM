package com.hospital_vm_cl.hospital_vm.service;

import com.hospital_vm_cl.hospital_vm.model.User;
import com.hospital_vm_cl.hospital_vm.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class UserService {

    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User guardar(User usuario) {
        if (usuario == null) {
            return null;
        }

        if (usuario.getRoles() == null) {
            usuario.setRoles(new ArrayList<>());
        }

        String email = usuario.getEmail().toLowerCase().trim();

        if (email.endsWith("@admin.hospitalduoc.cl")) {
            usuario.getRoles().add("ROLE_ADMIN");
        } else if (email.endsWith("@hospitalduoc.cl")) {
            usuario.getRoles().add("ROLE_MEDICO");
        } else {
            usuario.getRoles().add("ROLE_PACIENTE");
        }

        return userRepository.save(usuario);
    }
}