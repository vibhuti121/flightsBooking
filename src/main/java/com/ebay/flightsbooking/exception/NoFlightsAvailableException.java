package com.ebay.flightsbooking.exception;

public class NoFlightsAvailableException extends RuntimeException {
    public NoFlightsAvailableException(String message) {
        super(message);
    }
}
