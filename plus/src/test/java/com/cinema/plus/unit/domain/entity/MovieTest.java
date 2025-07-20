package com.cinema.plus.unit.domain.entity;

import com.cinema.plus.domain.entity.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class MovieTest {

    @Test
    @DisplayName("Should create movie with builder")
    void shouldCreateMovieWithBuilder() {
        // Act
        Movie movie = Movie.builder()
                .id(1L)
                .title("Test Movie")
                .overview("Test Overview")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .voteAverage(8.0)
                .posterPath("/test.jpg")
                .genres(Arrays.asList("Action", "Drama"))
                .favorite(false)
                .build();

        assertThat(movie.getId()).isEqualTo(1L);
        assertThat(movie.getTitle()).isEqualTo("Test Movie");
        assertThat(movie.getOverview()).isEqualTo("Test Overview");
        assertThat(movie.getReleaseDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(movie.getVoteAverage()).isEqualTo(8.0);
        assertThat(movie.getPosterPath()).isEqualTo("/test.jpg");
        assertThat(movie.getGenres()).containsExactly("Action", "Drama");
        assertThat(movie.isFavorite()).isFalse();
    }

    @ParameterizedTest
    @CsvSource({
            "8.0, true",
            "7.5, true",
            "7.4, false",
            "5.0, false"
    })
    @DisplayName("Should determine if movie is highly rated")
    void shouldDetermineIfMovieIsHighlyRated(double rating, boolean expected) {
        // Arrange
        Movie movie = Movie.builder()
                .voteAverage(rating)
                .build();

        boolean result = movie.isHighlyRated();

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should handle null vote average")
    void shouldHandleNullVoteAverage() {
        Movie movie = Movie.builder()
                .voteAverage(null)
                .build();

        boolean result = movie.isHighlyRated();

        assertThat(result).isFalse();
    }
}