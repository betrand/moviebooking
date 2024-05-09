package com.example.moviebooking.report;

import com.example.moviebooking.model.Booking;
import com.example.moviebooking.service.BookingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ReportingTask {

    private final BookingService bookingService;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public ReportingTask(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Scheduled(fixedRate = 30000) // For every 30 seconds
    public void runReport() {

        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            log.info("No bookings to report.\n");
            return;
        }

        final double totalRevenue = bookings.stream()
                .mapToDouble(b -> b.getTotalCost() + b.getTotalTax())
                .sum();

        final Booking highestRevenueBooking = bookings.stream()
                .max(Comparator.comparingDouble(b -> b.getTotalCost() + b.getTotalTax()))
                .orElse(null);

        Map<String, List<Booking>> bookingsByMovie = bookings.stream()
                .collect(Collectors.groupingBy(Booking::getMovieName));

        log.info("Total revenue (including tax) across all movies: {}", df.format(totalRevenue));

        if (highestRevenueBooking != null) {
            log.info("Movie that has got the highest revenue (including tax) in a single booking: {}\n", highestRevenueBooking.getMovieName());
        }

        bookingsByMovie.forEach((movieName, movieBookings) -> {

            double revenue = movieBookings.stream()
                    .mapToDouble(b -> b.getTotalCost() + b.getTotalTax())
                    .sum();

            log.info("");

            log.info("Movie Name: {}", movieName);
            log.info("Total revenue (including tax) across all bookings: {}", df.format(revenue));
            log.info("Bookings:");

            movieBookings.forEach(b ->
                    log.info("{} | {} | {}",
                            b.getDateTime(),
                            b.getNumberOfSeats(),
                            df.format(b.getTotalCost() + b.getTotalTax()))
            );
        });

        log.info("\n");
    }
}
