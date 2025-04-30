package com.ritik.service.impl;

import com.ritik.domain.BookingStatus;
import com.ritik.dto.BookingRequest;
import com.ritik.dto.SalonDTO;
import com.ritik.dto.ServiceDTO;
import com.ritik.dto.UserDTO;
import com.ritik.modal.Booking;
import com.ritik.modal.SalonReport;
import com.ritik.repository.BookingRepository;
import com.ritik.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    @Override
    public Booking createBooking(BookingRequest bookingRequest,
                                 UserDTO userDTO,
                                 SalonDTO salonDTO,
                                 Set<ServiceDTO> serviceDTOSet) throws Exception {
        int totalDuration = serviceDTOSet.stream()
                .mapToInt(ServiceDTO::getDuration)
                .sum();
        LocalDateTime bookingStartTime = bookingRequest.getStartTime();
        LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(totalDuration);

        isTimeSlotAvailable(salonDTO, bookingStartTime, bookingEndTime);


        int totalPrice = serviceDTOSet
                .stream()
                .mapToInt(ServiceDTO::getPrice)
                .sum();

        Set<Long> idList = serviceDTOSet.stream()
                .map(ServiceDTO::getId).collect(Collectors.toSet());
        Booking newBooking = new Booking();
        newBooking.setCustomerId(userDTO.getId());
        newBooking.setSalonId(salonDTO.getId());
        newBooking.setServiceIds(idList);
        newBooking.setStatus(BookingStatus.PENDING);
        newBooking.setStartTime(bookingStartTime);
        newBooking.setEndTime(bookingEndTime);
        newBooking.setTotalPrice(totalPrice);
        return bookingRepository.save(newBooking);
    }

    public void isTimeSlotAvailable(SalonDTO salonDTO,
                                       LocalDateTime bookingStartTime,
                                       LocalDateTime bookingEndTime) throws Exception {

        List<Booking> existingBookings = bookingRepository.findBySalonId(salonDTO.getId());

        LocalDateTime salonOpenTime = salonDTO.getOpenTime().atDate(bookingStartTime.toLocalDate());
        LocalDateTime salonCloseTime = salonDTO.getCloseTime().atDate(bookingEndTime.toLocalDate());
        if(bookingStartTime.isBefore(salonOpenTime) && bookingEndTime.isAfter(salonCloseTime)){

            throw new Exception("Booking time must be in salon open hours.");
        }

        for(Booking existingBooking : existingBookings){
            LocalDateTime existingStartTime = existingBooking.getStartTime();
            LocalDateTime existingEndTime = existingBooking.getEndTime();
            if(bookingStartTime.isBefore(existingEndTime) && bookingEndTime.isAfter(existingStartTime)) {
                throw new Exception("Slot not available, please try another time slot for booking in salon.");
            }
            if(bookingStartTime.isEqual(existingStartTime) || bookingEndTime.isEqual(existingEndTime)) {
                throw new Exception("Slot not available, please try another time slot for booking in salon.");
            }
        }
    }

    @Override
    public List<Booking> getBookingByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> getBookingBySalon(Long salonId) {
        return bookingRepository.findBySalonId(salonId);
    }

    @Override
    public Booking getBookingById(Long bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if(booking == null){
            throw new Exception("Booking not found with id :: " + bookingId);
        }
        return booking;
    }

    @Override
    public Booking updateBooking(Long bookingId, BookingStatus status) throws Exception {
        Booking existingBooking = getBookingById(bookingId);
        if(existingBooking == null){
            throw new Exception("Booking not found with id :: " + bookingId);
        }
        existingBooking.setStatus(status);
        return bookingRepository.save(existingBooking);
    }

    @Override
    public List<Booking> getBookingsByDate(LocalDate date, Long salonId) {
        List<Booking> allBookings = bookingRepository.findBySalonId(salonId);
        if(date == null){
            return allBookings;
        }
        return allBookings.stream().
                filter(booking ->
                        isSameDate(booking.getStartTime(),date) ||
                        isSameDate(booking.getEndTime(), date))
                .collect(Collectors.toList());
    }

    private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
        return dateTime.toLocalDate().isEqual(date);
    }

    @Override
    public SalonReport getSalonReport(Long salonId) {
        List<Booking> bookings = bookingRepository.findBySalonId(salonId);
        Double totalEarnings = bookings
                .stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();

        int totalBookings = bookings.size();
        List<Booking> cancelledBookings = bookings
                .stream()
                .filter(booking -> booking.getStatus().equals(BookingStatus.CANCELLED))
                .toList();
        Double totalRefund = cancelledBookings.stream()
                .mapToDouble(Booking::getTotalPrice)
                .sum();

        SalonReport salonReport = new SalonReport();
        salonReport.setId(salonId);
        salonReport.setTotalBookings(totalBookings);
        salonReport.setCancelledBookings(cancelledBookings.size());
        salonReport.setTotalEarning(totalEarnings);
        salonReport.setTotalRefund(totalRefund);

        return salonReport;
    }
}
