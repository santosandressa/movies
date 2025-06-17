package com.cinema.plus.domain.ports.repository;

import com.cinema.plus.domain.entity.Movie;

import java.util.List;
import java.util.Optional;

/*
    Porta de saída do hexágono da aplicação - Princípio de Inversão de Dependência (SOLID)
*/
public interface MovieRepository {
    List<Movie> findPopularMovies(int page);
    List<Movie> searchMovies(String query, int page);
    Optional<Movie> findMovieById(Long id);
    Movie saveAsFavorite(Movie movie);
    void removeFromFavorites(Long id);
    List<Movie> getFavorites();
}
