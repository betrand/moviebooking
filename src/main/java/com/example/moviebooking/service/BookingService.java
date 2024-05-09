package com.example.moviebooking.service;

import com.example.moviebooking.config.DataStore;
import com.example.moviebooking.exception.model.BookingException;
import com.example.moviebooking.model.Booking;
import com.example.moviebooking.model.Movie;
import com.example.moviebooking.request.BookingRequest;
import com.example.moviebooking.response.BookingResponse;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.time.LocalDateTime.now;

@Service
public class BookingService {

     private final DataStore dataStore;

     @Autowired
    public BookingService(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Synchronized
    public BookingResponse bookSeats(BookingRequest request) {

         //BookingRequest validation
        if (request.getNoOfSeats() < 1 || request.getNoOfSeats() > 4) {
            throw new BookingException("Invalid number of seats requested", HttpStatus.BAD_REQUEST.value());
        }

        //Find the movie from our movieDB and perform validation
        final Movie movie = dataStore.findMovie(request.getMovieName());

        if (movie == null) {
            throw new BookingException("Invalid movie name", HttpStatus.BAD_REQUEST.value());
        }

        if (movie.getAvailableSeats().get() < request.getNoOfSeats()) {
            throw new BookingException("Not enough seats available", HttpStatus.BAD_REQUEST.value());
        }

        // Decrement seats atomically
        movie.getAvailableSeats().addAndGet(-request.getNoOfSeats());

        // Calculate totalCost
        final double totalCost = movie.getPricePerSeat() * request.getNoOfSeats();

        // Calculate tax
        final double tax = totalCost * (movie.getTaxPercentagePerSeat() / 100);

        //Store the movie booking request
        final Booking booking = new Booking(now(), movie.getMovieName(), request.getNoOfSeats(), totalCost, tax);
        dataStore.addBooking(booking);

        //Return the BookingResponse
        return new BookingResponse(booking.getNumberOfSeats() + " number of seats are booked for " + booking.getMovieName());
    }

    public List<Booking> getAllBookings() {
         return dataStore.getAllBookings();
    }
}
