package com.cinema.plus.presentation.dto;

import java.time.LocalDate;
import java.util.List;

public record MovieDTO(
        Long id,
        String title,
        String overview,
        LocalDate releaseDate,
        Double voteAverage,
        String posterPath,
        List<String> genres,
        boolean favorite,
        boolean highlyRated
) { }
