package com.ebay.flightsbooking.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.ebay.flightsbooking.dto.BookingRequest;
import com.ebay.flightsbooking.exception.InvalidBookingException;
import com.ebay.flightsbooking.model.Flight;
import com.ebay.flightsbooking.model.Meal;
import com.ebay.flightsbooking.model.Passenger;
import com.ebay.flightsbooking.model.Seat;

@Component
public class BookingRequestValidator {

    public void validate(BookingRequest request, Flight flight) {
        if (request.getPassengers() == null || request.getPassengers().isEmpty()) {
            throw new InvalidBookingException("At least one passenger is required.");
        }
        if (request.getSeatSelections().size() != request.getPassengers().size()) {
            throw new InvalidBookingException("Every passenger must have one seat selection.");
        }
        validatePassengers(request.getPassengers());
        validateSeats(request.getSeatSelections(), flight.getListOfSeats(), request.getPassengers());
        validateMeals(request.getMealSelections(), flight.getListOfMeals(), request.getPassengers());
    }

    private void validatePassengers(List<Passenger> passengers) {
        Set<String> paxIds = new HashSet<>();
        for (Passenger passenger : passengers) {
            if (passenger.getPaxId() == null || passenger.getPaxId().isBlank()) {
                throw new InvalidBookingException("Passenger id is required for each passenger.");
            }
            if (!paxIds.add(passenger.getPaxId())) {
                throw new InvalidBookingException("Passenger ids must be unique.");
            }
        }
    }

    private void validateSeats(Map<String, String> seatSelections, List<Seat> seats, List<Passenger> passengers) {
        Set<String> availableSeats = new HashSet<>();
        for (Seat seat : seats) {
            if (seat.isAvailability()) {
                availableSeats.add(seat.getSeatNumber());
            }
        }

        Set<String> paxIds = new HashSet<>();
        for (Passenger passenger : passengers) {
            paxIds.add(passenger.getPaxId());
        }

        Set<String> chosenSeats = new HashSet<>();
        for (Map.Entry<String, String> entry : seatSelections.entrySet()) {
            if (entry.getKey() == null || entry.getKey().isBlank()) {
                throw new InvalidBookingException("Seat selection must be mapped to a passenger id.");
            }
            if (!paxIds.contains(entry.getKey())) {
                throw new InvalidBookingException("Seat selection contains unknown passenger " + entry.getKey() + ".");
            }
            if (!chosenSeats.add(entry.getValue())) {
                throw new InvalidBookingException("A seat can only be assigned once per booking.");
            }
            if (!availableSeats.contains(entry.getValue())) {
                throw new InvalidBookingException("Seat " + entry.getValue() + " is not available.");
            }
        }
    }

    private void validateMeals(Map<String, List<String>> mealSelections, List<Meal> meals, List<Passenger> passengers) {
        Set<String> mealCodes = new HashSet<>();
        for (Meal meal : meals) {
            mealCodes.add(meal.getMealCode());
        }

        Set<String> paxIds = new HashSet<>();
        for (Passenger passenger : passengers) {
            paxIds.add(passenger.getPaxId());
        }

        for (Map.Entry<String, List<String>> entry : mealSelections.entrySet()) {
            if (!paxIds.contains(entry.getKey())) {
                throw new InvalidBookingException("Meal selection contains unknown passenger " + entry.getKey() + ".");
            }
            for (String mealCode : entry.getValue()) {
                if (!mealCodes.contains(mealCode)) {
                    throw new InvalidBookingException("Meal " + mealCode + " is not available for this flight.");
                }
            }
        }
    }
}
