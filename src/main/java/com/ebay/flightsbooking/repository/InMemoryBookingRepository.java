package com.ebay.flightsbooking.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.ebay.flightsbooking.model.Booking;

@Repository
public class InMemoryBookingRepository implements BookingRepository {

    private final ConcurrentMap<Long, Booking> bookings = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    @Override
    public Booking save(Booking booking) {
        Long id = booking.getId() == null ? idGenerator.incrementAndGet() : booking.getId();
        Booking storedBooking = booking.toBuilder().id(id).build();
        bookings.put(id, storedBooking);
        return storedBooking;
    }

    @Override
    public List<Booking> findAll() {
        return new ArrayList<>(bookings.values());
    }
}
