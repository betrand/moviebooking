package com.example.moviebooking.servicve;

import com.example.moviebooking.config.DataStore;
import com.example.moviebooking.exception.model.BookingException;
import com.example.moviebooking.model.Movie;
import com.example.moviebooking.request.BookingRequest;
import com.example.moviebooking.response.BookingResponse;
import com.example.moviebooking.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    DataStore dataStore;

    BookingService bookingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        bookingService = new BookingService(dataStore);
    }

    @Test
    void test_bookSeats_with1_bookSeat_withSuccess() {

        //given
        Movie movie = Movie.builder()
                .availableSeats(new AtomicInteger(8))
                .movieName("Titanic")
                .pricePerSeat(20.00)
                .taxPercentagePerSeat(15.00)
                .build();

        BookingRequest bookingRequest = BookingRequest.builder()
                .movieName("Titanic") // Titanic is valid movie
                .noOfSeats(1)  // 1 is valid between min and maximum
                .build();

        BookingResponse expectedResponse = BookingResponse.builder()
                .bookingResponse("1 number of seats are booked for Titanic")
                .build();

        when(dataStore.findMovie(any()))
                .thenReturn(movie);

        BookingResponse actualResponse = bookingService.bookSeats(bookingRequest);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(dataStore).findMovie("Titanic");

    }

    @Test
    void test_bookSeats_with4_bookSeats_withSuccess() {

        //given
        Movie movie = Movie.builder()
                .availableSeats(new AtomicInteger(12))
                .movieName("Ironman")
                .pricePerSeat(25.00)
                .taxPercentagePerSeat(10.00)
                .build();

        BookingRequest bookingRequest = BookingRequest.builder()
                .movieName("Ironman") // Ironman is valid movie
                .noOfSeats(4)  // 4 is valid between min and maximum
                .build();

        BookingResponse expectedResponse = BookingResponse.builder()
                .bookingResponse("4 number of seats are booked for Ironman")
                .build();

        when(dataStore.findMovie(any()))
                .thenReturn(movie);

        BookingResponse actualResponse = bookingService.bookSeats(bookingRequest);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
        verify(dataStore).findMovie("Ironman");
    }

    @Test
    void test_bookSeats_with0_failsWith_invalidNoOfSeatsRequested() {

        //given
        BookingRequest bookingRequest = BookingRequest.builder()
                .movieName("Ironman") // Ironman is valid movie
                .noOfSeats(0)  // 0 is less than min
                .build();

        BookingException expectedException = new BookingException("Invalid number of seats requested", 400);

        BookingException actualResponse = assertThrows(BookingException.class, () ->
                bookingService.bookSeats(bookingRequest)
        );

        assertNotNull(actualResponse);
        assertEquals(expectedException, actualResponse);
        verify(dataStore, never()).findMovie("Ironman");
        assertEquals(expectedException.getMessage(), actualResponse.getMessage());
        assertEquals(expectedException.getStatus(), actualResponse.getStatus());

    }

    @Test
    void test_bookSeats_with5_failsWith_invalidNoOfSeatsRequested() {

        //given
        BookingRequest bookingRequest = BookingRequest.builder()
                .movieName("Titanic") // Titanic is valid movie
                .noOfSeats(5)  // 5 is less than min
                .build();

        BookingException expectedException = new BookingException("Invalid number of seats requested", 400);

        BookingException actualResponse = assertThrows(BookingException.class, () ->
                bookingService.bookSeats(bookingRequest)
        );

        assertNotNull(actualResponse);
        assertEquals(expectedException, actualResponse);
        verify(dataStore, never()).findMovie("Titanic");
        assertEquals(expectedException.getMessage(), actualResponse.getMessage());
        assertEquals(expectedException.getStatus(), actualResponse.getStatus());
    }

    @Test
    void test_bookSeats_withInvalidMovieName_returns400_invalidMovieName() {

        //given
        BookingRequest bookingRequest = BookingRequest.builder()
                .movieName("Wrong_Movie_Name") // Wrong_Movie_Name is not a valid movie name
                .noOfSeats(2)   // 2 seats is valid but
                .build();

        BookingException expectedException = new BookingException("Invalid movie name", 400);

        BookingException actualResponse = assertThrows(BookingException.class, () ->
                bookingService.bookSeats(bookingRequest)
        );

        assertNotNull(actualResponse);
        assertEquals(expectedException, actualResponse);
        verify(dataStore).findMovie("Wrong_Movie_Name");
        assertEquals(expectedException.getMessage(), actualResponse.getMessage());
        assertEquals(expectedException.getStatus(), actualResponse.getStatus());
    }

}
