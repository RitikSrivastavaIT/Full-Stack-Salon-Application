package com.ritik.service;

import com.ritik.domain.BookingStatus;
import com.ritik.dto.BookingRequest;
import com.ritik.dto.SalonDTO;
import com.ritik.dto.ServiceDTO;
import com.ritik.dto.UserDTO;
import com.ritik.modal.Booking;
import com.ritik.modal.SalonReport;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


public interface BookingService {

    Booking createBooking(BookingRequest bookingRequest,
                          UserDTO userDTO,
                          SalonDTO salonDTO,
                          Set<ServiceDTO> serviceDTOS) throws Exception;

    List<Booking> getBookingByCustomer(Long customerId);
    List<Booking> getBookingBySalon(Long salonId);
    Booking getBookingById(Long bookingId) throws Exception;
    Booking updateBooking(Long bookingId, BookingStatus status) throws Exception;
    List<Booking> getBookingsByDate(LocalDate date, Long salonId);
    SalonReport getSalonReport(Long salonId);

}
