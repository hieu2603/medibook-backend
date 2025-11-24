package com.sgu.clinic_service.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "appointment-service")
public interface AppointmentClient {
//    @GetMapping("/api/appointments/{id}")
//    ResponseEntity<>
}
