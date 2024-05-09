package com.example.moviebooking.controller;

import com.example.moviebooking.exception.model.BookingException;
import com.example.moviebooking.request.BookingRequest;
import com.example.moviebooking.response.BookingResponse;
import com.example.moviebooking.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class BookingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookingService bookingService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void test_bookSeats_with1_return200_success() throws Exception {

        BookingRequest request = BookingRequest.builder()
                .movieName("Titanic") // Titanic is valid movie
                .noOfSeats(1)  // 1 is valid between min and maximum
                .build();

        BookingResponse bookingResponse = BookingResponse.builder()
                .bookingResponse("1 number of seats are booked for Titanic")
                .build();

        when(bookingService.bookSeats(any())).thenReturn(bookingResponse);

        mockMvc.perform(post("/api/v1/movie-booking/bookSeats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(bookingResponse)))
                .andExpect(jsonPath("$.bookingResponse", is("1 number of seats are booked for Titanic")));

        verify(bookingService).bookSeats(request);
    }


    @Test
    void test_bookSeats_with4_return200_success() throws Exception {

        BookingRequest request = BookingRequest.builder()
                .movieName("Ironman") // Ironman is valid movie
                .noOfSeats(4)  // 4 is valid between min and maximum
                .build();

        BookingResponse bookingResponse = BookingResponse.builder()
                .bookingResponse("4 number of seats are booked for Ironman")
                .build();

        when(bookingService.bookSeats(any())).thenReturn(bookingResponse);

        mockMvc.perform(post("/api/v1/movie-booking/bookSeats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(bookingResponse)))
                .andExpect(jsonPath("$.bookingResponse", is("4 number of seats are booked for Ironman")));

        verify(bookingService).bookSeats(request);
    }


    @Test
    void test_bookSeats_with0_returns400_invalidNoOfSeatsRequested() throws Exception {

        BookingRequest request = BookingRequest.builder()
                .movieName("Titanic") // Titanic is valid movie but
                .noOfSeats(0) // 0 is less than min
                .build();

        BookingException bookingException = new BookingException("Invalid number of seats requested", 400);

        when(bookingService.bookSeats(any())).thenThrow(bookingException);

        mockMvc.perform(post("/api/v1/movie-booking/bookSeats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Invalid number of seats requested")));

        verify(bookingService).bookSeats(request);
    }

    @Test
    void test_bookSeats_with5_returns400_invalidNoOfSeatsRequested() throws Exception {

        BookingRequest request = BookingRequest.builder()
                .movieName("Ironman") // Titanic is valid movie but
                .noOfSeats(5) // 5 is more than maximum according to requirement
                .build();

        BookingException bookingException = new BookingException("Invalid number of seats requested", 400);

        when(bookingService.bookSeats(any())).thenThrow(bookingException);

        mockMvc.perform(post("/api/v1/movie-booking/bookSeats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Invalid number of seats requested")));

        verify(bookingService).bookSeats(request);
    }

    @Test
    void test_bookSeats_withInvalidMovieName_returns400_invalidMovieName() throws Exception {

        BookingRequest request = BookingRequest.builder()
                .noOfSeats(2) // 2 seats is valid but
                .movieName("Wrong_Movie_Name") // Wrong_Movie_Name is not a valid movie name
                .build();

        BookingException bookingException = new BookingException("Invalid movie name", 400);

        when(bookingService.bookSeats(any())).thenThrow(bookingException);

        mockMvc.perform(post("/api/v1/movie-booking/bookSeats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Invalid movie name")));

        verify(bookingService).bookSeats(request);
    }


}
