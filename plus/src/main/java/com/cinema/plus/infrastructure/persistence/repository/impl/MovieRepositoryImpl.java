package com.cinema.plus.infrastructure.persistence.repository.impl;

import com.cinema.plus.domain.entity.Movie;
import com.cinema.plus.domain.ports.repository.MovieRepository;
import com.cinema.plus.infrastructure.persistence.client.TmdbClient;
import com.cinema.plus.infrastructure.persistence.client.model.TmdbMovieDetailResponse;
import com.cinema.plus.infrastructure.config.TmdbProperties;
import com.cinema.plus.infrastructure.entity.MovieEntity;
import com.cinema.plus.infrastructure.mapper.MovieMapper;
import com.cinema.plus.infrastructure.persistence.repository.MovieJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

// Implementação do repositório - adaptador que implementa a porta definida no domínio
// Aplicação do Princípio de Segregação de Interface (I do SOLID)
@Repository
@RequiredArgsConstructor
@Slf4j
public class MovieRepositoryImpl implements MovieRepository {
    private final TmdbClient tmdbClient;
    private final TmdbProperties tmdbProperties;
    private final MovieJpaRepository movieJpaRepository;
    private final MovieMapper movieMapper;

    @Override
    public List<Movie> findPopularMovies(int page) {
        return tmdbClient.getPopularMovies(
                        tmdbProperties.getApiKey(),
                        page,
                        tmdbProperties.getLanguage())
                .getResults()
                .stream()
                .map(tmdbMovie -> {

                    var movie =  Movie.builder()
                            .id(tmdbMovie.getId())
                            .title(tmdbMovie.getTitle())
                            .overview(tmdbMovie.getOverview())
                            .voteAverage(tmdbMovie.getVoteAverage())
                            .posterPath(tmdbProperties.getImageBaseUrl() + tmdbMovie.getPosterPath())
                            .build();
                    try {
                        if (tmdbMovie.getReleaseDate() != null && !tmdbMovie.getReleaseDate().isEmpty()) {
                            movie.setReleaseDate(LocalDate.parse(tmdbMovie.getReleaseDate(), DateTimeFormatter.ISO_DATE));
                        }
                    } catch (DateTimeParseException e) {
                        log.warn("Unable to parse release date: {}", tmdbMovie.getReleaseDate());
                    }

                    // Verificar se o filme está na lista de favoritos
                    movie.setFavorite(movieJpaRepository.existsById(movie.getId()));

                    return movie;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Movie> searchMovies(String query, int page) {
        return tmdbClient.searchMovies(
                        tmdbProperties.getApiKey(),
                        query,
                        page,
                        tmdbProperties.getLanguage())
                .getResults()
                .stream()
                .map(tmdbMovie -> {
                    var movie =  Movie.builder()
                            .id(tmdbMovie.getId())
                            .title(tmdbMovie.getTitle())
                            .overview(tmdbMovie.getOverview())
                            .voteAverage(tmdbMovie.getVoteAverage())
                            .posterPath(tmdbProperties.getImageBaseUrl() + tmdbMovie.getPosterPath())
                            .build();
                    try {
                        if (tmdbMovie.getReleaseDate() != null && !tmdbMovie.getReleaseDate().isEmpty()) {
                            movie.setReleaseDate(LocalDate.parse(tmdbMovie.getReleaseDate(), DateTimeFormatter.ISO_DATE));
                        }
                    } catch (DateTimeParseException e) {
                        log.warn("Unable to parse release date: {}", tmdbMovie.getReleaseDate());
                    }

                    // Verificar se o filme está na lista de favoritos
                    movie.setFavorite(movieJpaRepository.existsById(movie.getId()));

                    return movie;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Movie> findMovieById(Long id) {
        try {
            var response = tmdbClient.getMovieDetails(
                    id,
                    tmdbProperties.getApiKey(),
                    tmdbProperties.getLanguage());
            var movie =  Movie.builder()
                    .id(response.getId())
                    .title(response.getTitle())
                    .overview(response.getOverview())
                    .voteAverage(response.getVoteAverage())
                    .posterPath(tmdbProperties.getImageBaseUrl() + response.getPosterPath())
                    .build();

            List<String> genres = response.getGenres() != null ?
                    response.getGenres().stream()
                            .map(TmdbMovieDetailResponse.Genre::getName)
                            .collect(Collectors.toList()) :
                    List.of();
            movie.setGenres(genres);

            try {
                if (response.getReleaseDate() != null && !response.getReleaseDate().isEmpty()) {
                    movie.setReleaseDate(LocalDate.parse(response.getReleaseDate(), DateTimeFormatter.ISO_DATE));
                }
            } catch (DateTimeParseException e) {
                log.warn("Unable to parse release date: {}", response.getReleaseDate());
            }
            movie.setFavorite(movieJpaRepository.existsById(movie.getId()));

            return Optional.of(movie);

        } catch (Exception e) {
            log.error("Error fetching movie details for ID: {}", id, e);
            return Optional.empty();
        }
    }

    @Override
    public Movie saveAsFavorite(Movie movie) {
        MovieEntity entity = movieMapper.toEntity(movie);
        entity = movieJpaRepository.save(entity);
        return movieMapper.toDomain(entity);
    }

    @Override
    public void removeFromFavorites(Long id) {
        movieJpaRepository.deleteById(id);
    }

    @Override
    public List<Movie> getFavorites() {
        return movieJpaRepository.findAll().stream()
                .map(movieMapper::toDomain)
                .collect(Collectors.toList());
    }
}
