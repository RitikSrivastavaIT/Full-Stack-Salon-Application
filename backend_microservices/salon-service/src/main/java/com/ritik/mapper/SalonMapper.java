package com.ritik.mapper;

import com.ritik.modal.Salon;
import com.ritik.payload.dto.SalonDTO;

public class SalonMapper {

    public static SalonDTO mapToSalonDTO(Salon salon){
        SalonDTO salonDTO = new SalonDTO();

        salonDTO.setId(salon.getId());
        salonDTO.setName(salon.getName());
        salonDTO.setAddress(salon.getAddress());
        salonDTO.setCity(salon.getCity());
        salonDTO.setImages(salon.getImages());
        salonDTO.setPhoneNumber(salon.getPhoneNumber());
        salonDTO.setEmail(salon.getEmail());
        salonDTO.setOwnerId(salon.getOwnerId());
        salonDTO.setOpenTime(salon.getOpenTime());
        salonDTO.setCloseTime(salon.getCloseTime());

        return salonDTO;
    }

}
