package com.ebay.flightsbooking.repository;

import java.util.List;
import java.util.Optional;

import com.ebay.flightsbooking.model.Flight;

public interface FlightRepository {
    Flight save(Flight flight);

    Optional<Flight> findById(Long id);

    List<Flight> findAll();
}
