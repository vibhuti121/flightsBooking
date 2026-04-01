package com.ebay.flightsbooking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FlightReviewResponse {
    Long flightId;
    String flightNumber;
    String airlineName;
    String provider;
    String origin;
    String destination;
    BigDecimal basePrice;
    LocalDateTime departureDate;
    String cancellationPolicy;
    String modificationPolicy;
    Integer totalAvailableSeats;
}
