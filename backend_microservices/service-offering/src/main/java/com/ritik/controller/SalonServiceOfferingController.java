package com.ritik.controller;

import com.ritik.dto.CategoryDTO;
import com.ritik.dto.SalonDTO;
import com.ritik.dto.ServiceDTO;
import com.ritik.modal.ServiceOffering;
import com.ritik.service.ServiceOfferingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service-offering/salon-owner")
public class SalonServiceOfferingController {
    private final ServiceOfferingService serviceOfferingService;

    @PostMapping
    public ResponseEntity<ServiceOffering> createService(
            @RequestBody ServiceDTO serviceDTO
    ){
        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(1L);

        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(serviceDTO.getCategory());

        ServiceOffering serviceOffering = serviceOfferingService.createService(salonDTO, serviceDTO, categoryDTO);

        return new ResponseEntity<ServiceOffering>(serviceOffering, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ServiceOffering> updateService(
            @PathVariable Long id,
            @RequestBody  ServiceOffering serviceOffering
    ) throws Exception {

        ServiceOffering serviceOfferings = serviceOfferingService.updateService(id, serviceOffering);

        return ResponseEntity.ok(serviceOfferings);
    }
}
