package com.hospital_vm_cl.hospital_vm.service;

import org.springframework.stereotype.Service;

@Service
public class JwtService {
    public String generateToken(String email) {
        return "token-generado-para-" + email;
    }
}