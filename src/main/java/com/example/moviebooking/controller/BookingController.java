package com.example.moviebooking.controller;

import com.example.moviebooking.request.BookingRequest;
import com.example.moviebooking.response.BookingResponse;
import com.example.moviebooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movie-booking")
public class BookingController {

    private BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/bookSeats")
    public BookingResponse bookSeats(@RequestBody BookingRequest request) {

        return bookingService.bookSeats(request);
    }


}
