package com.ebay.flightsbooking.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    private Long id;
    private Long flightId;
    @Builder.Default
    private Map<String, String> listOfSeats = new LinkedHashMap<>();
    @Builder.Default
    private List<Passenger> listOfPax = new ArrayList<>();
    private String customerMail;
    private String customerNumber;
    @Builder.Default
    private Map<String, List<String>> listOfMeals = new LinkedHashMap<>();
    private LocalDateTime bookingDate;
    private LocalDate travelDate;
}
