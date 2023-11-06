package io.oduck.api.domain.studio.repository;

import com.querydsl.core.Fetchable;
import io.oduck.api.domain.studio.entity.Studio;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudioRepository extends JpaRepository<Studio, Long> {

  boolean existsByName(String name);

  Optional<Studio> findByIdAndDeletedAtIsNull(Long studioId);

  List<Studio> findAllByDeletedAtIsNull();
}
