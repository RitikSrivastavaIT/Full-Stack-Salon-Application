package com.ritik.mapper;

import com.ritik.dto.BookingDTO;
import com.ritik.modal.Booking;

public class BookingMapper {

    public static BookingDTO mapToBookingDTO(Booking booking){
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCustomerId(booking.getCustomerId());
        bookingDTO.setSalonId(booking.getSalonId());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setEndTime(booking.getEndTime());
        bookingDTO.setStatus(booking.getStatus());
        bookingDTO.setServiceIds(booking.getServiceIds());
        return bookingDTO;
    }
}
