package com.ritik.dto;

import com.ritik.domain.BookingStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDTO {

    private Long id;

    private Long salonId;
    private Long CustomerId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Set<Long> serviceIds;

    private BookingStatus status = BookingStatus.PENDING;

    private Integer totalPrice ;
}
