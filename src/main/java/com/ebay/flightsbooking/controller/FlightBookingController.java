package com.ebay.flightsbooking.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ebay.flightsbooking.dto.BookingRequest;
import com.ebay.flightsbooking.dto.FlightReviewResponse;
import com.ebay.flightsbooking.model.Booking;
import com.ebay.flightsbooking.model.Flight;
import com.ebay.flightsbooking.model.Meal;
import com.ebay.flightsbooking.model.Seat;
import com.ebay.flightsbooking.service.BookingService;
import com.ebay.flightsbooking.service.FlightService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class FlightBookingController {

    private final FlightService flightService;
    private final BookingService bookingService;

    @GetMapping("/review/price")
    public FlightReviewResponse reviewPrice(@RequestParam Long flightId) {
        return flightService.getPriceReview(flightId);
    }

    @GetMapping("/avaialble/seats")
    public List<Seat> getAvailableSeats(@RequestParam Long flightId) {
        return flightService.getAvailableSeats(flightId);
    }

    @GetMapping("/avaialble/meals")
    public List<Meal> getAvailableMeals(@RequestParam Long flightId) {
        return flightService.getAvailableMeals(flightId);
    }

    @GetMapping("/available/flights")
    public List<Flight> getAvailableFlights(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate) {
        return flightService.getAvailableFlights(origin, destination, travelDate);
    }

    @GetMapping("/bookings")
    public List<Booking> getBookings() {
        return bookingService.getBookings();
    }

    @PostMapping("/confirm/booking")
    public Booking confirmBooking(@RequestBody BookingRequest bookingRequest) {
        return bookingService.confirmBooking(bookingRequest);
    }
}
