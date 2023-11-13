package io.oduck.api.domain.genre.repository;

import io.oduck.api.domain.genre.entity.Genre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GenreRepository extends JpaRepository<Genre, Long> {

  boolean existsByName(String name);

  @Query("select distinct g from Genre g join fetch g.animeGenres where g.id = :id")
  Optional<Genre> findByIdAndDeletedAtIsNull(@Param("id") Long genreId);

  List<Genre> findAllByDeletedAtIsNull();
}
