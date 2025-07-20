package com.cinema.plus.unit.presentation.controller;

import com.cinema.plus.application.service.MovieService;
import com.cinema.plus.domain.entity.Movie;
import com.cinema.plus.presentation.controller.MovieController;
import com.cinema.plus.presentation.dto.MovieDTO;
import com.cinema.plus.presentation.dto.MovieListDTO;
import com.cinema.plus.presentation.dto.request.CreateFavoriteRequestDTO;
import com.cinema.plus.presentation.mapper.MovieRestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @Mock
    private MovieRestMapper movieMapper;

    @InjectMocks
    private MovieController movieController;

    private Movie testMovie;
    private MovieDTO testMovieDTO;

    @BeforeEach
    void setUp() {
        testMovie = Movie.builder()
                .id(1L)
                .title("Test Movie")
                .overview("Test Overview")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .voteAverage(8.0)
                .posterPath("/test.jpg")
                .genres(Arrays.asList("Action", "Drama"))
                .favorite(false)
                .build();

        testMovieDTO = new MovieDTO(
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
    void shouldGetPopularMovies() {
        // Arrange
        List<Movie> movies = Collections.singletonList(testMovie);
        when(movieService.getPopularMovies(1)).thenReturn(movies);
        when(movieMapper.toResponse(testMovie)).thenReturn(testMovieDTO);

        // Act
        ResponseEntity<MovieListDTO> response = movieController.getPopularMovies(1);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().movies()).hasSize(1);
        assertThat(response.getBody().page()).isEqualTo(1);
        verify(movieService).getPopularMovies(1);
    }

    @Test
    @DisplayName("Should search movies")
    void shouldSearchMovies() {
        // Arrange
        List<Movie> movies = Collections.singletonList(testMovie);
        when(movieService.searchMovies("test", 1)).thenReturn(movies);
        when(movieMapper.toResponse(testMovie)).thenReturn(testMovieDTO);

        // Act
        ResponseEntity<MovieListDTO> response = movieController.searchMovies("test", 1);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().movies()).hasSize(1);
        assertThat(response.getBody().page()).isEqualTo(1);
        verify(movieService).searchMovies("test", 1);
    }

    @Test
    @DisplayName("Should get movie details")
    void shouldGetMovieDetails() {
        // Arrange
        when(movieService.getMovieDetails(1L)).thenReturn(Optional.of(testMovie));
        when(movieMapper.toResponse(testMovie)).thenReturn(testMovieDTO);

        // Act
        ResponseEntity<EntityModel<MovieDTO>> response = movieController.getMovieDetails(1L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).isEqualTo(testMovieDTO);
        verify(movieService).getMovieDetails(1L);
    }

    @Test
    @DisplayName("Should return 404 when movie not found")
    void shouldReturn404WhenMovieNotFound() {
        // Arrange
        when(movieService.getMovieDetails(999L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<EntityModel<MovieDTO>> response = movieController.getMovieDetails(999L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(movieService).getMovieDetails(999L);
    }

    @Test
    @DisplayName("Should add movie to favorites")
    void shouldAddMovieToFavorites() {
        // Arrange
        CreateFavoriteRequestDTO requestDTO = new CreateFavoriteRequestDTO(1L);
        Movie favoriteMovie = Movie.builder()
                .id(1L)
                .title("Test Movie")
                .overview("Test Overview")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .voteAverage(8.0)
                .posterPath("/test.jpg")
                .genres(Arrays.asList("Action", "Drama"))
                .favorite(true)
                .build();

        MovieDTO favoriteMovieDTO = new MovieDTO(
                1L,
                "Test Movie",
                "Test Overview",
                LocalDate.of(2023, 1, 1),
                8.0,
                "/test.jpg",
                Arrays.asList("Action", "Drama"),
                true,
                false
        );

        when(movieService.addToFavorites(1L)).thenReturn(favoriteMovie);
        when(movieMapper.toResponse(favoriteMovie)).thenReturn(favoriteMovieDTO);

        // Act
        ResponseEntity<EntityModel<MovieDTO>> response = movieController.addFavorite(requestDTO);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).isEqualTo(favoriteMovieDTO);
        verify(movieService).addToFavorites(1L);
    }

    @Test
    @DisplayName("Should remove movie from favorites")
    void shouldRemoveMovieFromFavorites() {
        // Act
        ResponseEntity<Void> response = movieController.removeFavorite(1L);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(movieService).removeFromFavorites(1L);
    }

    @Test
    @DisplayName("Should get favorite movies")
    void shouldGetFavoriteMovies() {
        // Arrange
        List<Movie> movies = Collections.singletonList(testMovie);
        when(movieService.getFavorites()).thenReturn(movies);
        when(movieMapper.toResponse(testMovie)).thenReturn(testMovieDTO);

        // Act
        ResponseEntity<MovieListDTO> response = movieController.getFavorites();

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().movies()).hasSize(1);
        verify(movieService).getFavorites();
    }
}
