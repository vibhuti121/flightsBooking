package com.ebay.flightsbooking.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ebay.flightsbooking.dto.BookingRequest;
import com.ebay.flightsbooking.exception.InvalidBookingException;
import com.ebay.flightsbooking.model.Booking;
import com.ebay.flightsbooking.model.Flight;
import com.ebay.flightsbooking.model.Seat;
import com.ebay.flightsbooking.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultBookingService implements BookingService {

    private final BookingRepository bookingRepository;
    private final FlightService flightService;
    private final BookingRequestValidator bookingRequestValidator;

    @Override
    public List<Booking> getBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking confirmBooking(BookingRequest request) {
        Flight flight = flightService.getFlight(request.getFlightId());
        if (request.getTravelDate() != null && !flight.getDepartureDate().toLocalDate().isEqual(request.getTravelDate())) {
            throw new InvalidBookingException("Travel date must match the selected flight departure date.");
        }

        synchronized (flight) {
            bookingRequestValidator.validate(request, flight);
            Flight updatedFlight = reserveSeats(flight, request.getSeatSelections());
            Booking booking = buildBooking(request, flight);
            Booking savedBooking = bookingRepository.save(booking);
            flightService.saveFlight(updatedFlight);
            return savedBooking;
        }
    }

    private Flight reserveSeats(Flight flight, Map<String, String> seatSelections) {
        List<Seat> updatedSeats = new ArrayList<>();
        for (Seat seat : flight.getListOfSeats()) {
            if (seatSelections.containsValue(seat.getSeatNumber())) {
                updatedSeats.add(seat.toBuilder().availability(false).build());
            } else {
                updatedSeats.add(seat);
            }
        }

        return flight.toBuilder()
                .listOfSeats(updatedSeats)
                .totalAvailableSeats(flight.getTotalAvailableSeats() - seatSelections.size())
                .build();
    }

    private Booking buildBooking(BookingRequest request, Flight flight) {
        return Booking.builder()
                .flightId(request.getFlightId())
                .listOfSeats(request.getSeatSelections())
                .listOfPax(request.getPassengers())
                .customerMail(request.getCustomerMail())
                .customerNumber(request.getCustomerNumber())
                .listOfMeals(request.getMealSelections())
                .bookingDate(LocalDateTime.now())
                .travelDate(request.getTravelDate() == null ? flight.getDepartureDate().toLocalDate() : request.getTravelDate())
                .build();
    }
}
