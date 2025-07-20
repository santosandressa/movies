package com.cinema.plus.unit.application.service;

import com.cinema.plus.application.service.MovieService;
import com.cinema.plus.domain.entity.Movie;
import com.cinema.plus.domain.ports.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    private Movie testMovie;

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
    }

    @Test
    @DisplayName("Should return popular movies")
    void shouldReturnPopularMovies() {
        List<Movie> expectedMovies = Arrays.asList(testMovie);
        when(movieRepository.findPopularMovies(1)).thenReturn(expectedMovies);

        List<Movie> actualMovies = movieService.getPopularMovies(1);

        assertThat(actualMovies).isEqualTo(expectedMovies);
        verify(movieRepository).findPopularMovies(1);
    }

    @Test
    @DisplayName("Should search movies")
    void shouldSearchMovies() {
        List<Movie> expectedMovies = Arrays.asList(testMovie);
        when(movieRepository.searchMovies("test", 1)).thenReturn(expectedMovies);

        List<Movie> actualMovies = movieService.searchMovies("test", 1);

        assertThat(actualMovies).isEqualTo(expectedMovies);
        verify(movieRepository).searchMovies("test", 1);
    }

    @Test
    @DisplayName("Should get movie details")
    void shouldGetMovieDetails() {
        when(movieRepository.findMovieById(1L)).thenReturn(Optional.of(testMovie));

        Optional<Movie> actualMovie = movieService.getMovieDetails(1L);

        assertThat(actualMovie).isPresent();
        assertThat(actualMovie.get()).isEqualTo(testMovie);
        verify(movieRepository).findMovieById(1L);
    }

    @Test
    @DisplayName("Should add movie to favorites")
    void shouldAddMovieToFavorites() {
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

        when(movieRepository.findMovieById(1L)).thenReturn(Optional.of(testMovie));
        when(movieRepository.saveAsFavorite(any(Movie.class))).thenReturn(favoriteMovie);

        Movie result = movieService.addToFavorites(1L);

        assertThat(result).isEqualTo(favoriteMovie);
        assertThat(result.isFavorite()).isTrue();
        verify(movieRepository).findMovieById(1L);
        verify(movieRepository).saveAsFavorite(any(Movie.class));
    }

    @Test
    @DisplayName("Should throw exception when adding non-existent movie to favorites")
    void shouldThrowExceptionWhenAddingNonExistentMovieToFavorites() {
        when(movieRepository.findMovieById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> movieService.addToFavorites(999L));
        verify(movieRepository).findMovieById(999L);
        verify(movieRepository, never()).saveAsFavorite(any(Movie.class));
    }

    @Test
    @DisplayName("Should remove movie from favorites")
    void shouldRemoveMovieFromFavorites() {
        movieService.removeFromFavorites(1L);

        verify(movieRepository).removeFromFavorites(1L);
    }

    @Test
    @DisplayName("Should get favorite movies")
    void shouldGetFavoriteMovies() {
        List<Movie> expectedMovies = Collections.singletonList(testMovie);
        when(movieRepository.getFavorites()).thenReturn(expectedMovies);

        List<Movie> actualMovies = movieService.getFavorites();

        assertThat(actualMovies).isEqualTo(expectedMovies);
        verify(movieRepository).getFavorites();
    }
}