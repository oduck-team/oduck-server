package io.oduck.api.domain.series.repository;

import io.oduck.api.domain.series.entity.Series;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> {

  boolean existsByTitle(String title);

  Optional<Series> findByIdAndDeletedAtIsNull(Long seriesId);

  List<Series> findAllByDeletedAtIsNull();
}
