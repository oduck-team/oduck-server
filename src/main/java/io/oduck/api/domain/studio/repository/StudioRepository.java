package io.oduck.api.domain.studio.repository;

import io.oduck.api.domain.studio.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudioRepository extends JpaRepository<Studio, Long> {

  boolean existsByName(String name);
}
