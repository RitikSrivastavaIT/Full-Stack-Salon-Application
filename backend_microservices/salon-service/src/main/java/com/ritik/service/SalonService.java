package com.ritik.service;

import com.ritik.modal.Salon;
import com.ritik.payload.dto.SalonDTO;
import com.ritik.payload.dto.UserDTO;

import java.util.List;

public interface SalonService {

    Salon createSalon(SalonDTO salonDTO, UserDTO userDTO);
    Salon updateSalon(SalonDTO salonDTO, UserDTO userDTO, Long salonId) throws Exception;
    List<Salon> getAllSalons() throws Exception;
    Salon getSalonById(Long salonId) throws Exception;
    Salon getSalonByOwnerId(Long ownerId);
    List<Salon> searchSalonByCityName(String city);
}
