package com.cinema.plus.application.service;

import com.cinema.plus.domain.entity.Movie;
import com.cinema.plus.domain.ports.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {

    // Injetamos a porta do repositório, não a implementação concreta
    // Aplicação do Princípio da Inversão de Dependência (D do SOLID)

    private final MovieRepository movieRepository;
    private final Logger log = LoggerFactory.getLogger(MovieService.class);

    // Casos de uso da aplicação
    public List<Movie> getPopularMovies(int page) {
        log.info("Getting popular movies for page {}", page);
        return movieRepository.findPopularMovies(page);
    }

    public List<Movie> searchMovies(String query, int page) {
        log.info("Searching movies with query '{}' on page {}", query, page);
        return movieRepository.searchMovies(query, page);
    }

    public Optional<Movie> getMovieDetails(Long id) {
        log.info("Getting details for movie with ID {}", id);
        return movieRepository.findMovieById(id);
    }

    public Movie addToFavorites(Long id) {
        log.info("Adding movie with ID {} to favorites", id);
        return movieRepository.findMovieById(id)
                .map(movie -> {
                    movie.setFavorite(true);
                    return movieRepository.saveAsFavorite(movie);
                })
                .orElseThrow(() -> new RuntimeException("Movie not found with ID: " + id));
    }

    public void removeFromFavorites(Long id) {
        log.info("Removing movie with ID {} from favorites", id);
        movieRepository.removeFromFavorites(id);
    }

    public List<Movie> getFavorites() {
        log.info("Getting all favorite movies");
        return movieRepository.getFavorites();
    }
}
