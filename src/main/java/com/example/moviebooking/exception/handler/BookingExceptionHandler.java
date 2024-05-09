package com.example.moviebooking.exception.handler;

import com.example.moviebooking.exception.model.BookingException;
import com.example.moviebooking.exception.model.ExceptionDetail;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BookingExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BookingException.class)
    public ResponseEntity<Object> handleBookingException(BookingException ex) {

        ExceptionDetail detail = new ExceptionDetail(
                ex.getMessage(),
                ex.getStatus());

        return new ResponseEntity<>(detail, HttpStatus.valueOf(ex.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {

        ExceptionDetail detail = new ExceptionDetail("Server Error", 500);

        return new ResponseEntity<>(detail, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
