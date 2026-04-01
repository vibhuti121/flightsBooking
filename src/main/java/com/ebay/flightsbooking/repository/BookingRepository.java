package com.ebay.flightsbooking.repository;

import java.util.List;

import com.ebay.flightsbooking.model.Booking;

public interface BookingRepository {
    Booking save(Booking booking);

    List<Booking> findAll();
}
