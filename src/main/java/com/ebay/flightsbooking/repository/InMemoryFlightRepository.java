package com.ebay.flightsbooking.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.ebay.flightsbooking.model.Flight;

@Repository
public class InMemoryFlightRepository implements FlightRepository {

    private final ConcurrentMap<Long, Flight> flights = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public Flight save(Flight flight) {
        Long id = flight.getId() == null ? idGenerator.incrementAndGet() : flight.getId();
        Flight storedFlight = flight.toBuilder().id(id).build();
        flights.put(id, storedFlight);
        return storedFlight;
    }

    @Override
    public Optional<Flight> findById(Long id) {
        return Optional.ofNullable(flights.get(id));
    }

    @Override
    public List<Flight> findAll() {
        return new ArrayList<>(flights.values());
    }
}
