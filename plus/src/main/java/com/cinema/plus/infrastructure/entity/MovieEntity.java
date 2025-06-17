package com.cinema.plus.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "favorite_movies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String overview;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    @Column(name = "vote_average")
    private Double voteAverage;

    @Column(name = "poster_path")
    private String posterPath;

    @Column(name = "genres", length = 500)
    private String genres;
}