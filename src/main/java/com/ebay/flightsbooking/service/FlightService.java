package com.ebay.flightsbooking.service;

import java.time.LocalDate;
import java.util.List;

import com.ebay.flightsbooking.dto.FlightReviewResponse;
import com.ebay.flightsbooking.model.Flight;
import com.ebay.flightsbooking.model.Meal;
import com.ebay.flightsbooking.model.Seat;

public interface FlightService {
    List<Flight> getAvailableFlights(String origin, String destination, LocalDate travelDate);

    FlightReviewResponse getPriceReview(Long flightId);

    List<Seat> getAvailableSeats(Long flightId);

    List<Meal> getAvailableMeals(Long flightId);

    Flight getFlight(Long flightId);

    Flight saveFlight(Flight flight);
}
