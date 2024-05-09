package com.example.moviebooking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    private LocalDateTime dateTime;
    private String movieName;
    private int numberOfSeats;
    private double totalCost;
    private double totalTax;

}
