package io.oduck.api.domain.series.repository;

import io.oduck.api.domain.series.entity.Series;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface SeriesRepository extends JpaRepository<Series, Long> {

  boolean existsByTitle(String title);

  @Query("select distinct s from Series s join fetch s.animes where s.id = :id")
  Optional<Series> findByIdAndDeletedAtIsNull(@Param("id") Long seriesId);

  List<Series> findAllByDeletedAtIsNull();
}
