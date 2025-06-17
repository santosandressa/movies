package com.cinema.plus.presentation.mapper;

import com.cinema.plus.domain.entity.Movie;
import com.cinema.plus.presentation.dto.MovieDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieRestMapper {

    @Mapping(target = "highlyRated", expression = "java(movie.isHighlyRated())")
    MovieDTO toResponse(Movie movie);
}
