package com.cinema.plus.infrastructure.persistence.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TmdbMovieDetailResponse {
    private Long id;
    private String title;
    private String overview;

    @JsonProperty("release_date")
    private String releaseDate;

    @JsonProperty("vote_average")
    private Double voteAverage;

    @JsonProperty("poster_path")
    private String posterPath;

    private List<Genre> genres;

    @Data
    public static class Genre {
        private Integer id;
        private String name;
    }
}