package com.ebay.flightsbooking.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Seat {
    private String seatNumber;
    private BigDecimal price;
    private String description;
    private SeatType seatType;
    private boolean availability;
}
