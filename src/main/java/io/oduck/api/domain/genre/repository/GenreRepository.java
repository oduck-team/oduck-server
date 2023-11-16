package io.oduck.api.domain.genre.repository;

import io.oduck.api.domain.genre.entity.Genre;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GenreRepository extends JpaRepository<Genre, Long>, GenreRepositoryCustom {

  @Query("select distinct g from Genre g left join fetch g.animeGenres where g.id = :id and g.deletedAt = null")
  Optional<Genre> findByIdAndDeletedAtIsNull(@Param("id") Long genreId);

  List<Genre> findAllByDeletedAtIsNull();
}
