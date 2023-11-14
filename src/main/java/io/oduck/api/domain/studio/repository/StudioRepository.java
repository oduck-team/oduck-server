package io.oduck.api.domain.studio.repository;

import io.oduck.api.domain.studio.entity.Studio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudioRepository extends JpaRepository<Studio, Long>, StudioRepositoryCustom {

  @Query("select distinct s from Studio s left join fetch s.animeStudios where s.id = :id and s.deletedAt = null")
  Optional<Studio> findByIdAndDeletedAtIsNull(@Param("id") Long studioId);

  List<Studio> findAllByDeletedAtIsNull();
}
