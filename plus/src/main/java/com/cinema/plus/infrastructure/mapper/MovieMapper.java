package com.cinema.plus.infrastructure.mapper;

import com.cinema.plus.domain.entity.Movie;
import com.cinema.plus.infrastructure.entity.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    @Mapping(source = "genres", target = "genres", qualifiedByName = "stringToGenresList")
    Movie toDomain(MovieEntity entity);

    @Mapping(source = "genres", target = "genres", qualifiedByName = "genresListToString")
    MovieEntity toEntity(Movie domain);

    @Named("stringToGenresList")
    default List<String> stringToGenresList(String genres) {
        if (genres == null || genres.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(genres.split(","));
    }

    @Named("genresListToString")
    default String genresListToString(List<String> genres) {
        if (genres == null || genres.isEmpty()) {
            return "";
        }
        return String.join(",", genres);
    }
}
