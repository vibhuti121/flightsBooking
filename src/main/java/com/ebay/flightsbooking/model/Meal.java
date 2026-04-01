package com.ebay.flightsbooking.model;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Meal {
    private String mealName;
    private String mealCode;
    private BigDecimal price;
    private MealType mealType;
}
