package io.oduck.api.domain.originalAuthor.repository;

import com.querydsl.core.Fetchable;
import io.oduck.api.domain.originalAuthor.entity.OriginalAuthor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OriginalAuthorRepository extends JpaRepository<OriginalAuthor, Long> {

  boolean existsByName(String name);

  Optional<OriginalAuthor> findByIdAndDeletedAtIsNull(Long originalAuthorId);

  List<OriginalAuthor> findAllByDeletedAtIsNull();
}
