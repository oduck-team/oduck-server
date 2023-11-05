package io.oduck.api.domain.genre.repository;

import io.oduck.api.domain.genre.entity.Genre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {

  boolean existsByName(String name);

  Optional<Genre> findByIdAndDeletedAtIsNull(Long genreId);

  List<Genre> findAllByDeletedAtIsNull();
}
