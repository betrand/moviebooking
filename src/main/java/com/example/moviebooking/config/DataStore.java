package com.example.moviebooking.config;

import com.example.moviebooking.model.Booking;
import com.example.moviebooking.model.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.ApplicationScope;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.example.moviebooking.util.FileUtil.readFile;

@Slf4j
@ApplicationScope
@Configuration
public class DataStore {

    private final Map<String, Movie> movieDb = new HashMap();

    private final List<Booking> bookings = new CopyOnWriteArrayList<>(); // to support current access

    @PostConstruct
    public void loadMovies() throws IOException {
        final String path = "src/main/resources/json/movies.json";

        List<Movie> movieList = readFile(path, new TypeReference<List<Movie>>(){});

        log.info("");
        log.info("Loaded movies: {}", new ObjectMapper().writeValueAsString(movieList));

        movieList.forEach(movie -> { movieDb.put(movie.getMovieName(), movie);});
    }

    public Movie findMovie(String movieName) {
        return movieDb.get(movieName);
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public List<Booking> getAllBookings() {
        return bookings;
    }
}
