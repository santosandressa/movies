package com.cinema.plus.infrastructure.persistence.client;

import com.cinema.plus.infrastructure.persistence.client.model.TmdbMovieDetailResponse;
import com.cinema.plus.infrastructure.persistence.client.model.TmdbMovieResponse;
import com.cinema.plus.infrastructure.persistence.client.model.TmdbPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "tmdb-api", url = "${api.themoviedb.base-url}")
public interface TmdbClient {

    @GetMapping("/movie/popular")
    TmdbPageResponse<TmdbMovieResponse> getPopularMovies(
            @RequestParam("api_key") String apiKey,
            @RequestParam("page") int page,
            @RequestParam("language") String language);

    @GetMapping("/search/movie")
    TmdbPageResponse<TmdbMovieResponse> searchMovies(
            @RequestParam("api_key") String apiKey,
            @RequestParam("query") String query,
            @RequestParam("page") int page,
            @RequestParam("language") String language);

    @GetMapping("/movie/{id}")
    TmdbMovieDetailResponse getMovieDetails(
            @PathVariable("id") Long id,
            @RequestParam("api_key") String apiKey,
            @RequestParam("language") String language);
}
