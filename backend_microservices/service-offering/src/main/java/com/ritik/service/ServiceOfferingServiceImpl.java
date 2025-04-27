package com.ritik.service;

import com.ritik.dto.CategoryDTO;
import com.ritik.dto.SalonDTO;
import com.ritik.dto.ServiceDTO;
import com.ritik.modal.ServiceOffering;
import com.ritik.repository.ServiceOfferingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceOfferingServiceImpl implements ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;

    @Override
    public ServiceOffering createService(SalonDTO salonDTO,
                                         ServiceDTO serviceDTO,
                                         CategoryDTO categoryDTO) {
        ServiceOffering serviceOffering = new ServiceOffering();
        serviceOffering.setSalonId(salonDTO.getId());
        serviceOffering.setName(serviceDTO.getName());
        serviceOffering.setDescription(serviceDTO.getDescription());
        serviceOffering.setPrice(serviceDTO.getPrice());
        serviceOffering.setImage(serviceDTO.getImage());
        serviceOffering.setCategoryId(categoryDTO.getId());
        serviceOffering.setDuration(serviceDTO.getDuration());
        return serviceOfferingRepository.save(serviceOffering);
    }

    @Override
    public ServiceOffering updateService(Long serviceId,
                                         ServiceOffering service) throws Exception {
        ServiceOffering existingService = serviceOfferingRepository
                .findById(serviceId)
                .orElse(null);
        if(existingService == null){
            throw new Exception("Service not exist with id :: " + serviceId);
        }
        existingService.setName(service.getName());
        existingService.setDescription(service.getDescription());
        existingService.setPrice(service.getPrice());
        existingService.setImage(service.getImage());
        existingService.setDuration(service.getDuration());
        return serviceOfferingRepository.save(existingService);
    }

    @Override
    public Set<ServiceOffering> getAllServicesBySalonId(Long salonId, Long categoryId) {
        Set<ServiceOffering> services =  serviceOfferingRepository.findBySalonId(salonId);
        if(categoryId != null){
            services = services.stream().filter((service) -> service.getCategoryId() != null &&
                    service.getCategoryId().equals(categoryId)).collect(Collectors.toSet());
        }
        return services;
    }


    @Override
    public Set<ServiceOffering> getAllServicesById(Set<Long> serviceIds) {
       List<ServiceOffering> services = serviceOfferingRepository.findAllById(serviceIds);
        return new HashSet<>(services);
    }

    @Override
    public ServiceOffering getServiceById(Long id) throws Exception {
        ServiceOffering service = serviceOfferingRepository.findById(id).orElse(null);
        if(service == null){
            throw new Exception("Service not found with id :: " + id);
        }
        return service;
    }
}
