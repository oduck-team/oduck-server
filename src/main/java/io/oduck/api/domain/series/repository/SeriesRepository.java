package io.oduck.api.domain.series.repository;

import io.oduck.api.domain.series.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> {

}
