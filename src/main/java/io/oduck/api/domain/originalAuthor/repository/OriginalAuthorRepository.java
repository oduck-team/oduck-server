package io.oduck.api.domain.originalAuthor.repository;

import io.oduck.api.domain.originalAuthor.entity.OriginalAuthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OriginalAuthorRepository extends JpaRepository<OriginalAuthor, Long> {

}
