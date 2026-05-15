package com.hospital_vm_cl.hospital_vm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "servicio-externo", url = "https://jsonplaceholder.typicode.com")
public interface ExternoClient {
    
    @GetMapping("/posts/{id}")
    Object obtenerValidacionExterna(@PathVariable("id") Long id);
}