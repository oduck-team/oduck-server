package io.oduck.api.domain.originalAuthor.repository;

import io.oduck.api.domain.originalAuthor.entity.OriginalAuthor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OriginalAuthorRepository extends JpaRepository<OriginalAuthor, Long> {

  boolean existsByName(String name);

  @Query("select distinct oa from OriginalAuthor oa join fetch oa.animeOriginalAuthors where oa.id = :id")
  Optional<OriginalAuthor> findByIdAndDeletedAtIsNull(@Param("id") Long originalAuthorId);

  List<OriginalAuthor> findAllByDeletedAtIsNull();
}
