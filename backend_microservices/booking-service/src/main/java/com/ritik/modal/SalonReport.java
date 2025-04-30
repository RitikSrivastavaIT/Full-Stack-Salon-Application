package com.ritik.modal;

import lombok.Data;

@Data
public class SalonReport {
    private Long id;
    private String SalonName;
    private Double totalEarning;
    private Integer totalBookings;
    private Integer cancelledBookings;
    private Double totalRefund;
}
