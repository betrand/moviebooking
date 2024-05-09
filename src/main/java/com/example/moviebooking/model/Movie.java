package com.example.moviebooking.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    private String movieName;
    private AtomicInteger availableSeats; // Thread-safe for concurrent access since many booking will reduce the number
    private double pricePerSeat;
    private  double taxPercentagePerSeat;

}
