package io.oduck.api.domain.genre.repository;

import io.oduck.api.domain.genre.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

  boolean existsByName(String name);
}
