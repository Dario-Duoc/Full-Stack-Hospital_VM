package com.hospital_vm_cl.hospital_vm.controller;

import com.hospital_vm_cl.hospital_vm.model.User;
import com.hospital_vm_cl.hospital_vm.dto.LoginRequest;
import com.hospital_vm_cl.hospital_vm.dto.AuthResponse;
import com.hospital_vm_cl.hospital_vm.repository.UserRepository;
import com.hospital_vm_cl.hospital_vm.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        logger.info("Intento de login: {}", request.getEmail()); 

        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(request.getPassword())) {
                String token = jwtService.generateToken(user.getEmail()); 
                return ResponseEntity.ok(new AuthResponse(token));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password incorrecta");
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
    }
}