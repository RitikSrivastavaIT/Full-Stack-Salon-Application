package com.ritik.controller;

import com.ritik.domain.BookingStatus;
import com.ritik.dto.*;
import com.ritik.mapper.BookingMapper;
import com.ritik.modal.Booking;
import com.ritik.modal.SalonReport;
import com.ritik.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestParam Long salonId,
            @RequestBody BookingRequest bookingRequest
    ) throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);

        SalonDTO salonDTO = new SalonDTO();
        salonDTO.setId(salonId);
        salonDTO.setOpenTime(LocalTime.now());
        salonDTO.setCloseTime(LocalTime.now().plusHours(12));

        Set<ServiceDTO> serviceDTOSet = new HashSet<>();

        ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(1L);
        serviceDTO.setName("Salon Cleaning");
        serviceDTO.setDescription("Salon Cleaning");
        serviceDTO.setPrice(399);
        serviceDTO.setDuration(45);

        serviceDTOSet.add(serviceDTO);

        Booking booking = bookingService.createBooking(bookingRequest,userDTO,salonDTO,serviceDTOSet);

        return ResponseEntity.ok(booking);
    }

    @GetMapping("/customer")
    public ResponseEntity<Set<BookingDTO>> getBookingByCustomer(){

        List<Booking> bookings = bookingService.getBookingByCustomer(1L);
        return ResponseEntity.ok(bookingToBookingDTO(bookings));
    }

    @GetMapping("/salon")
    public ResponseEntity<Set<BookingDTO>> getBookingBySalon(){

        List<Booking> bookings = bookingService.getBookingBySalon(1L);

        return ResponseEntity.ok(bookingToBookingDTO(bookings));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBookingById(
            @PathVariable Long bookingId
    ) throws Exception {
        Booking booking = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(BookingMapper.mapToBookingDTO(booking));
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<BookingDTO> updateBookingStatus(
            @PathVariable Long bookingId,
            @RequestParam BookingStatus status
    ) throws Exception {
        Booking booking = bookingService.updateBooking(bookingId, status);
        return ResponseEntity.ok(BookingMapper.mapToBookingDTO(booking));
    }

    @GetMapping("/slots/salon/{salonId}/date/{date}")
    public ResponseEntity<List<BookingSlotDTO>> getBookedSlot(
            @PathVariable Long salonId,
            @RequestParam(required = false) LocalDate date
    ){
        List<Booking> bookings = bookingService.getBookingsByDate(date,salonId);
        List<BookingSlotDTO> bookingSlotDTOs = bookings
                .stream()
                .map(booking -> {
                    BookingSlotDTO bookingSlotDTO = new BookingSlotDTO();
                    bookingSlotDTO.setStartTime(booking.getStartTime());
                    bookingSlotDTO.setEndTime(booking.getEndTime());
                    return bookingSlotDTO;
                }).toList();
        return ResponseEntity.ok(bookingSlotDTOs);
    }

    @GetMapping("/report")
    public ResponseEntity<SalonReport> getSalonReport(){

        SalonReport salonReport = bookingService.getSalonReport(1L);

        return ResponseEntity.ok(salonReport);
    }

    private Set<BookingDTO> bookingToBookingDTO(List<Booking> bookings){
        return bookings.stream()
                .map(BookingMapper::mapToBookingDTO).collect(Collectors.toSet());
    }
}