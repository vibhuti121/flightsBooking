package com.ebay.flightsbooking.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiErrorResponse {
    LocalDateTime timestamp;
    String message;
}
