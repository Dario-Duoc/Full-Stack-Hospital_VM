package com.hospital_vm_cl.hospital_vm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class HospitalVmApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalVmApplication.class, args);
    }
}