package com.cinema.plus.presentation.dto;

import java.util.List;

public record MovieListDTO(
        List<MovieDTO> movies,
        int page,
        int totalPages
) { }
