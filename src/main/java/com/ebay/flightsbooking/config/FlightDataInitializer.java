package com.ebay.flightsbooking.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ebay.flightsbooking.model.Flight;
import com.ebay.flightsbooking.model.Meal;
import com.ebay.flightsbooking.model.MealType;
import com.ebay.flightsbooking.model.Seat;
import com.ebay.flightsbooking.model.SeatType;
import com.ebay.flightsbooking.repository.FlightRepository;
import com.ebay.flightsbooking.service.FlightService;

@Configuration
public class FlightDataInitializer {

    @Bean
    CommandLineRunner loadFlights(FlightService flightService, FlightRepository flightRepository) {
        return args -> {
            if (!flightRepository.findAll().isEmpty()) {
                return;
            }

            flightService.saveFlight(createFlight(
                    "AI-202",
                    "Air India",
                    "GDS-One",
                    "DEL",
                    "BLR",
                    LocalDateTime.of(2026, 4, 15, 9, 30),
                    new BigDecimal("6200.00")));
            flightService.saveFlight(createFlight(
                    "6E-451",
                    "IndiGo",
                    "DirectConnect",
                    "DEL",
                    "BOM",
                    LocalDateTime.of(2026, 4, 15, 13, 45),
                    new BigDecimal("5400.00")));
            flightService.saveFlight(createFlight(
                    "UK-881",
                    "Vistara",
                    "PartnerAPI",
                    "BLR",
                    "GOI",
                    LocalDateTime.of(2026, 4, 16, 7, 15),
                    new BigDecimal("4800.00")));
        };
    }

    private Flight createFlight(String flightNumber,
                                String airlineName,
                                String provider,
                                String origin,
                                String destination,
                                LocalDateTime departureDate,
                                BigDecimal price) {
        List<Seat> seats = List.of(
                createSeat("1A", "Extra legroom window", SeatType.WINDOW, "900.00"),
                createSeat("1B", "Extra legroom middle", SeatType.MIDDLE, "750.00"),
                createSeat("1C", "Extra legroom aisle", SeatType.AISLE, "850.00"),
                createSeat("2A", "Standard window", SeatType.WINDOW, "450.00"),
                createSeat("2B", "Standard middle", SeatType.MIDDLE, "300.00"),
                createSeat("2C", "Standard aisle", SeatType.AISLE, "400.00"));
        return Flight.builder()
                .flightsNumber(flightNumber)
                .airlineName(airlineName)
                .provider(provider)
                .origin(origin)
                .destination(destination)
                .price(price)
                .listOfSeats(seats)
                .seatsCount(seats.size())
                .listOfMeals(List.of(
                        createMeal("Vegetarian Meal", "VGML", "350.00", MealType.VEG),
                        createMeal("Chicken Meal", "CHML", "420.00", MealType.NON_VEG),
                        createMeal("Snacks Box", "SNBX", "180.00", MealType.VEG)))
                .departureDate(departureDate)
                .cancellationPolicy("Free cancellation up to 24 hours before departure, then airline fee applies.")
                .modificationPolicy("One date change allowed up to 12 hours before departure with fare difference.")
                .totalAvailableSeats(seats.size())
                .build();
    }

    private Seat createSeat(String seatNumber, String description, SeatType seatType, String price) {
        return Seat.builder()
                .seatNumber(seatNumber)
                .description(description)
                .seatType(seatType)
                .price(new BigDecimal(price))
                .availability(true)
                .build();
    }

    private Meal createMeal(String mealName, String mealCode, String price, MealType mealType) {
        return Meal.builder()
                .mealName(mealName)
                .mealCode(mealCode)
                .price(new BigDecimal(price))
                .mealType(mealType)
                .build();
    }
}
