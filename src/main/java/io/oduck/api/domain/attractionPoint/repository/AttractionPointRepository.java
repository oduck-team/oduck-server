package io.oduck.api.domain.attractionPoint.repository;

import io.oduck.api.domain.attractionPoint.entity.AttractionPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttractionPointRepository extends JpaRepository<AttractionPoint, Long>{

}
