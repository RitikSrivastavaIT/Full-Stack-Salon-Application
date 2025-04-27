package com.ritik.service;

import com.ritik.dto.CategoryDTO;
import com.ritik.dto.SalonDTO;
import com.ritik.dto.ServiceDTO;
import com.ritik.modal.ServiceOffering;

import java.util.Set;

public interface ServiceOfferingService {

    ServiceOffering createService(SalonDTO salonDTO,
                                  ServiceDTO serviceDTO,
                                  CategoryDTO categoryDTO);

    ServiceOffering updateService(Long serviceId, ServiceOffering service) throws Exception;

    Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId);

    Set<ServiceOffering> getAllServicesById(Set<Long> serviceIds);

    ServiceOffering getServiceById(Long id) throws Exception;
}


