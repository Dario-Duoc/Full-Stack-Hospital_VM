package com.hospital_vm_cl.hospital_vm.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}