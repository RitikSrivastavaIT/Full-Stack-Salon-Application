package com.ritik.service.impl;

import com.ritik.modal.Salon;
import com.ritik.payload.dto.SalonDTO;
import com.ritik.payload.dto.UserDTO;
import com.ritik.repository.SalonRepository;
import com.ritik.service.SalonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    @Override
    public Salon createSalon(SalonDTO request, UserDTO user) {

        Salon salon = new Salon();

        salon.setName(request.getName());
        salon.setAddress(request.getAddress());
        salon.setEmail(request.getEmail());
        salon.setCity(request.getCity());
        salon.setImages(request.getImages());
        salon.setOwnerId(user.getId());
        salon.setPhoneNumber(request.getPhoneNumber());
        salon.setOpenTime(request.getOpenTime());
        salon.setCloseTime(request.getCloseTime());
        
        return salonRepository.save(salon);
    }

    @Override
    public Salon updateSalon(SalonDTO salon, UserDTO user, Long salonId) throws Exception {
        Salon existingSalon = salonRepository.findById(salonId).orElse(null);
        if(existingSalon != null && existingSalon.getOwnerId().equals(user.getId())){
            existingSalon.setName(salon.getName());
            existingSalon.setAddress(salon.getAddress());
            existingSalon.setEmail(salon.getEmail());
            existingSalon.setCity(salon.getCity());
            existingSalon.setImages(salon.getImages());
            existingSalon.setOwnerId(user.getId());
            existingSalon.setPhoneNumber(salon.getPhoneNumber());
            existingSalon.setOpenTime(salon.getOpenTime());
            existingSalon.setCloseTime(salon.getCloseTime());

            return salonRepository.save(existingSalon);
        }

        throw new Exception("Salon does not exist.");
    }

    @Override
    public List<Salon> getAllSalons() throws Exception {
        return salonRepository.findAll();
    }

    @Override
    public Salon getSalonById(Long salonId) throws Exception {
        Salon salon = salonRepository.findById(salonId).orElse(null);
        if(salon == null){
            throw new Exception("Salon not exist.");
        }
        return salon;
    }

    @Override
    public Salon getSalonByOwnerId(Long ownerId) {
        return salonRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Salon> searchSalonByCityName(String city) {
        return salonRepository.searchSalons(city);
    }
}
