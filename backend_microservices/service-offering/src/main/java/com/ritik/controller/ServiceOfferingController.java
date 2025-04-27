package com.ritik.controller;

import com.ritik.modal.ServiceOffering;
import com.ritik.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/service-offering")
@RequiredArgsConstructor
public class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    @GetMapping("/salon/{salonId}")
    public ResponseEntity<Set<ServiceOffering>> getServicesBySalonId(
            @PathVariable Long salonId,
            @RequestParam(required = false) Long categoryId
    ){
        Set<ServiceOffering> serviceOfferings = serviceOfferingService.getAllServicesBySalonId(salonId, categoryId);

        return ResponseEntity.ok(serviceOfferings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOffering> getServicesById(
            @PathVariable Long id
    ) throws Exception {
        ServiceOffering serviceOffering = serviceOfferingService.getServiceById(id);

        return new ResponseEntity<>(serviceOffering,HttpStatus.OK);
    }

    @GetMapping("/list/{ids}")
    public ResponseEntity<Set<ServiceOffering>> getAllServicesByIds(
            @PathVariable Set<Long> ids
    ){
        Set<ServiceOffering> serviceOfferings = serviceOfferingService.getAllServicesById(ids);

        return ResponseEntity.ok(serviceOfferings);
    }
}
