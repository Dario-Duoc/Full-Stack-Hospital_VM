package com.hospital_vm_cl.hospital_vm.service;

import org.springframework.stereotype.Service;
import com.hospital_vm_cl.hospital_vm.model.User;
import com.hospital_vm_cl.hospital_vm.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        logger.info("UserService inicializado correctamente.");
    }

    public String prueba() {
        return "ok";
    }

    public Optional<User> buscarPorNombreUsuario(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario '" + username + "' no encontrado"));
    }

    public User save(User user) {
        logger.info("Registrando nuevo usuario en el sistema: {}", user.getUsername());
        return userRepository.save(user);
    }
}