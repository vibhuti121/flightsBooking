package com.ebay.flightsbooking.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ebay.flightsbooking.dto.ApiErrorResponse;
import com.ebay.flightsbooking.exception.InvalidBookingException;
import com.ebay.flightsbooking.exception.ResourceNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFound(ResourceNotFoundException exception) {
        return buildResponse(exception.getMessage());
    }

    @ExceptionHandler(InvalidBookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInvalidBooking(InvalidBookingException exception) {
        return buildResponse(exception.getMessage());
    }

    private ApiErrorResponse buildResponse(String message) {
        return ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .message(message)
                .build();
    }
}
