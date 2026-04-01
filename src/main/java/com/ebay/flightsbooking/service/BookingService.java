package com.ebay.flightsbooking.service;

import java.util.List;

import com.ebay.flightsbooking.dto.BookingRequest;
import com.ebay.flightsbooking.model.Booking;

public interface BookingService {
    List<Booking> getBookings();

    Booking confirmBooking(BookingRequest request);
}
