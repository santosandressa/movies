package com.cinema.plus.infrastructure.persistence.repository;

import com.cinema.plus.infrastructure.entity.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieJpaRepository extends JpaRepository<MovieEntity, Long> {
}