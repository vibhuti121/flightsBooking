package com.ebay.flightsbooking.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ebay.flightsbooking.model.Passenger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {
    private Long flightId;
    @Builder.Default
    private Map<String, String> seatSelections = new LinkedHashMap<>();
    @Builder.Default
    private List<Passenger> passengers = new ArrayList<>();
    private String customerMail;
    private String customerNumber;
    @Builder.Default
    private Map<String, List<String>> mealSelections = new LinkedHashMap<>();
    private LocalDate travelDate;
}
