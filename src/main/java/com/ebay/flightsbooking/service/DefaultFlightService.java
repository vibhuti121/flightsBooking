package com.ebay.flightsbooking.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ebay.flightsbooking.dto.FlightReviewResponse;
import com.ebay.flightsbooking.exception.ResourceNotFoundException;
import com.ebay.flightsbooking.model.Flight;
import com.ebay.flightsbooking.model.Meal;
import com.ebay.flightsbooking.model.Seat;
import com.ebay.flightsbooking.repository.FlightRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultFlightService implements FlightService {

    private final FlightRepository flightRepository;

    @Override
    public List<Flight> getAvailableFlights(String origin, String destination, LocalDate travelDate) {
        return flightRepository.findAll().stream()
                .filter(flight -> flight.getTotalAvailableSeats() > 0)
                .filter(flight -> origin == null || flight.getOrigin().equalsIgnoreCase(origin))
                .filter(flight -> destination == null || flight.getDestination().equalsIgnoreCase(destination))
                .filter(flight -> travelDate == null || flight.getDepartureDate().toLocalDate().isEqual(travelDate))
                .sorted(Comparator.comparing(Flight::getDepartureDate))
                .toList();
    }

    @Override
    public FlightReviewResponse getPriceReview(Long flightId) {
        Flight flight = getFlight(flightId);
        return FlightReviewResponse.builder()
                .flightId(flight.getId())
                .flightNumber(flight.getFlightsNumber())
                .airlineName(flight.getAirlineName())
                .provider(flight.getProvider())
                .origin(flight.getOrigin())
                .destination(flight.getDestination())
                .basePrice(flight.getPrice())
                .departureDate(flight.getDepartureDate())
                .cancellationPolicy(flight.getCancellationPolicy())
                .modificationPolicy(flight.getModificationPolicy())
                .totalAvailableSeats(flight.getTotalAvailableSeats())
                .build();
    }

    @Override
    public List<Seat> getAvailableSeats(Long flightId) {
        return getFlight(flightId).getListOfSeats().stream()
                .filter(Seat::isAvailability)
                .toList();
    }

    @Override
    public List<Meal> getAvailableMeals(Long flightId) {
        return getFlight(flightId).getListOfMeals();
    }

    @Override
    public Flight getFlight(Long flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new ResourceNotFoundException("Flight " + flightId + " not found."));
    }

    @Override
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }
}
