package com.ebay.flightsbooking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    private Long id;
    private String flightsNumber;
    private String airlineName;
    private String provider;
    private String origin;
    private String destination;
    private BigDecimal price;
    @Builder.Default
    private List<Seat> listOfSeats = new ArrayList<>();
    private int seatsCount;
    @Builder.Default
    private List<Meal> listOfMeals = new ArrayList<>();
    private LocalDateTime departureDate;
    private String cancellationPolicy;
    private String modificationPolicy;
    private int totalAvailableSeats;
}
