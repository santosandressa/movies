package com.cinema.plus.domain.entity;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Movie {
    private Long id;
    private String title;
    private String overview;
    private LocalDate releaseDate;
    private Double voteAverage;
    private String posterPath;
    private List<String> genres;
    private boolean favorite;

    public boolean isHighlyRated() {
        return voteAverage != null && voteAverage >= 7.5;
    }
}
