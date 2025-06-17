package com.cinema.plus.presentation.controller;

import com.cinema.plus.application.service.MovieService;
import com.cinema.plus.domain.entity.Movie;
import com.cinema.plus.presentation.dto.MovieDTO;
import com.cinema.plus.presentation.dto.MovieListDTO;
import com.cinema.plus.presentation.dto.request.CreateFavoriteRequestDTO;
import com.cinema.plus.presentation.mapper.MovieRestMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Validated
public class MovieController {
    private final Logger log = LoggerFactory.getLogger(MovieController.class);
    private final MovieService movieService;
    private final MovieRestMapper movieMapper;

    @GetMapping("/popular")
    public ResponseEntity<MovieListDTO> getPopularMovies(
            @RequestParam(defaultValue = "1") @Min(1) @Max(1000) int page) {

        log.info("REST request to get popular movies for page {}", page);

        List<Movie> movies = movieService.getPopularMovies(page);
        List<MovieDTO> responses = movies.stream()
                .map(movieMapper::toResponse)
                .toList();

        responses.forEach(this::addMovieLinks);

        return ResponseEntity.ok(new MovieListDTO(responses, page, 20));
    }

    @GetMapping("/search")
    public ResponseEntity<MovieListDTO> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") @Min(1) @Max(1000) int page) {

        log.info("REST request to search movies with query '{}' on page {}", query, page);

        List<Movie> movies = movieService.searchMovies(query, page);
        List<MovieDTO> responses = movies.stream()
                .map(movieMapper::toResponse)
                .collect(Collectors.toList());

        responses.forEach(this::addMovieLinks);

        return ResponseEntity.ok(new MovieListDTO(responses, page, 20));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<MovieDTO>> getMovieDetails(@PathVariable Long id) {
        log.info("REST request to get details for movie with ID {}", id);

        return movieService.getMovieDetails(id)
                .map(movie -> {
                    MovieDTO response = movieMapper.toResponse(movie);
                    addMovieLinks(response);

                    EntityModel<MovieDTO> entityModel = EntityModel.of(response);

                    return ResponseEntity.ok(entityModel);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/favorites")
    public ResponseEntity<EntityModel<MovieDTO>> addFavorite(@RequestBody @Valid CreateFavoriteRequestDTO request) {
        log.info("REST request to add movie with ID {} to favorites", request.movieId());

        Movie movie = movieService.addToFavorites(request.movieId());
        MovieDTO response = movieMapper.toResponse(movie);
        addMovieLinks(response);

        EntityModel<MovieDTO> entityModel = EntityModel.of(response);
        entityModel.add(linkTo(methodOn(MovieController.class).getFavorites()).withRel("favorites"));

        return ResponseEntity.ok(entityModel);
    }


    @DeleteMapping("/favorites/{id}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long id) {
        log.info("REST request to remove movie with ID {} from favorites", id);

        movieService.removeFromFavorites(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favorites")
    public ResponseEntity<MovieListDTO> getFavorites() {
        log.info("REST request to get all favorite movies");

        List<Movie> movies = movieService.getFavorites();
        List<MovieDTO> responses = movies.stream()
                .map(movieMapper::toResponse)
                .toList();

        responses.forEach(movie ->
                movie = addMovieLinks(movie)
        );

        return ResponseEntity.ok(new MovieListDTO(responses, 1, 1));
    }


    private MovieDTO addMovieLinks(MovieDTO movie) {
        Link selfLink = linkTo(methodOn(MovieController.class).getMovieDetails(movie.id())).withSelfRel();
        Link favoritesLink;

        if (movie.favorite()) {
            favoritesLink = linkTo(methodOn(MovieController.class).removeFavorite(movie.id())).withRel("remove-favorite");
        } else {
            favoritesLink = linkTo(methodOn(MovieController.class).addFavorite(new CreateFavoriteRequestDTO(movie.id()))).withRel("add-favorite");
        }

        return movie;
    }

}
