package com.ebay.flightsbooking;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ebay.flightsbooking.dto.BookingRequest;
import com.ebay.flightsbooking.exception.InvalidBookingException;
import com.ebay.flightsbooking.exception.NoFlightsAvailableException;
import com.ebay.flightsbooking.model.Booking;
import com.ebay.flightsbooking.model.Passenger;
import com.ebay.flightsbooking.service.BookingService;
import com.ebay.flightsbooking.service.FlightService;

@SpringBootTest
class FlightBookingServiceTest {

    @Autowired
    private FlightService flightService;

    @Autowired
    private BookingService bookingService;

    @Test
    void shouldLoadSeededFlights() {
        assertThat(flightService.getAvailableFlights(null, null, null))
                .hasSize(3);
    }

    @Test
    void shouldConfirmBookingAndReduceAvailableSeats() {
        Booking booking = bookingService.confirmBooking(BookingRequest.builder()
                .flightId(1L)
                .seatSelections(Map.of("P1", "1A", "P2", "1B"))
                .passengers(List.of(
                        Passenger.builder().paxId("P1").name("Alice").build(),
                        Passenger.builder().paxId("P2").name("Bob").build()))
                .customerMail("customer@test.com")
                .customerNumber("9999999999")
                .mealSelections(Map.of("P1", List.of("VGML"), "P2", List.of("CHML")))
                .travelDate(LocalDate.of(2026, 4, 15))
                .build());

        assertThat(booking.getId()).isEqualTo(1L);
        assertThat(booking.getListOfSeats()).containsEntry("P1", "1A");
        assertThat(flightService.getAvailableSeats(1L)).hasSize(4);
        assertThat(flightService.getPriceReview(1L).getTotalAvailableSeats()).isEqualTo(4);
    }

    @Test
    void shouldRejectPastTravelDateWhenSearchingFlights() {
        assertThatThrownBy(() -> flightService.getAvailableFlights("DEL", "BLR", LocalDate.of(2026, 3, 31)))
                .isInstanceOf(InvalidBookingException.class)
                .hasMessage("Travel date cannot be in the past. Please provide a valid future date.");
    }

    @Test
    void shouldShowMessageWhenNoFlightsMatchOriginDestination() {
        assertThatThrownBy(() -> flightService.getAvailableFlights("DEL", "MAA", LocalDate.of(2026, 4, 15)))
                .isInstanceOf(NoFlightsAvailableException.class)
                .hasMessage("No flights found for origin DEL, destination MAA on date 2026-04-15.");
    }
}
