package com.cinema.plus.integration;

import com.cinema.plus.application.service.MovieService;
import com.cinema.plus.domain.entity.Movie;
import com.cinema.plus.presentation.controller.MovieController;
import com.cinema.plus.presentation.dto.MovieDTO;
import com.cinema.plus.presentation.dto.request.CreateFavoriteRequestDTO;
import com.cinema.plus.presentation.mapper.MovieRestMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
@ContextConfiguration(classes = TestConfig.class)
@EnableWebMvc
@WithMockUser
@ImportAutoConfiguration(exclude = HypermediaAutoConfiguration.class)
class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRestMapper movieMapper;

    Movie movie;
    MovieDTO movieDTO;

    @BeforeEach
    void setUp() {
        movie = Movie.builder()
                .id(1L)
                .title("Test Movie")
                .overview("Test Overview")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .voteAverage(8.0)
                .posterPath("/test.jpg")
                .genres(Arrays.asList("Action", "Drama"))
                .favorite(false)
                .build();

        movieDTO = new MovieDTO(
                1L,
                "Test Movie",
                "Test Overview",
                LocalDate.of(2023, 1, 1),
                8.0,
                "/test.jpg",
                Arrays.asList("Action", "Drama"),
                false,
                false
        );
    }

    @Test
    @DisplayName("Should get popular movies")
    void shouldGetPopularMovies() throws Exception {
        when(movieService.getPopularMovies(1)).thenReturn(Collections.singletonList(movie));
        when(movieMapper.toResponse(movie)).thenReturn(movieDTO);

        mockMvc.perform(get("/api/movies/popular")
                        .param("page", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.movies").isArray());
    }

    @Test
    @DisplayName("Should add movie to favorites")
    void shouldAddMovieToFavorites() throws Exception {
        movie = Movie.builder()
                .id(1L)
                .title("Test Movie")
                .overview("Test Overview")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .voteAverage(8.0)
                .posterPath("/test.jpg")
                .genres(Arrays.asList("Action", "Drama"))
                .favorite(true)
                .build();

        CreateFavoriteRequestDTO requestDTO = new CreateFavoriteRequestDTO(1L);

        when(movieService.addToFavorites(1L)).thenReturn(movie);
        when(movieMapper.toResponse(movie)).thenReturn(movieDTO);

        mockMvc.perform(post("/api/movies/favorites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should get movie details")
    void shouldGetMovieDetails() throws Exception {
        movie = Movie.builder()
                .id(1L)
                .title("Test Movie")
                .overview("Test Overview")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .voteAverage(8.0)
                .posterPath("/test.jpg")
                .genres(Arrays.asList("Action", "Drama"))
                .favorite(false)
                .build();

        when(movieService.getMovieDetails(1L)).thenReturn(Optional.of(movie));
        when(movieMapper.toResponse(movie)).thenReturn(movieDTO);

        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1L));

    }

    @Test
    @DisplayName("Should return 404 when movie not found")
    void shouldReturn404WhenMovieNotFound() throws Exception {
        when(movieService.getMovieDetails(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/movies/999"))
                .andExpect(status().isNotFound());
    }
}