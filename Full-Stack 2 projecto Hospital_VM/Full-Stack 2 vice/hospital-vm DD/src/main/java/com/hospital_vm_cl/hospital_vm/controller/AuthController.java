package com.hospital_vm_cl.hospital_vm.controller;

import com.hospital_vm_cl.hospital_vm.model.User;
import com.hospital_vm_cl.hospital_vm.dto.LoginRequest;
import com.hospital_vm_cl.hospital_vm.dto.AuthResponse;
import com.hospital_vm_cl.hospital_vm.repository.UserRepository;
import com.hospital_vm_cl.hospital_vm.service.JwtService;
import com.hospital_vm_cl.hospital_vm.service.UserService;
import jakarta.validation.Valid;
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

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserService userService;

    // Actualizamos el constructor para recibir los 3 componentes
    public AuthController(UserRepository userRepository, JwtService jwtService, UserService userService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    // NUEVO ENDPOINT: Ahora Postman sí encontrará /api/auth/register
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        logger.info("Intentando registrar un nuevo usuario con correo: {}", user.getEmail());
        
        // Validamos si el email ya existe en la base de datos
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo ya está registrado");
        }
        
        // Guarda el usuario aplicando la lógica de dominios de correo para roles automáticos
        User nuevoUsuario = userService.guardar(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

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