package io.oduck.api.domain.originalAuthor.repository;

import io.oduck.api.domain.originalAuthor.entity.OriginalAuthor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OriginalAuthorRepository extends JpaRepository<OriginalAuthor, Long>, OriginalAuthorRepositoryCustom {

  @Query("select distinct oa from OriginalAuthor oa left join fetch oa.animeOriginalAuthors where oa.id = :id and  oa.deletedAt = null")
  Optional<OriginalAuthor> findByIdAndDeletedAtIsNull(@Param("id") Long originalAuthorId);

  List<OriginalAuthor> findAllByDeletedAtIsNull();
}
