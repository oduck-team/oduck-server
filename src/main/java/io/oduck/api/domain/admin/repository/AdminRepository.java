package io.oduck.api.domain.admin.repository;

import io.oduck.api.domain.admin.entity.Admin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query("select distinct a from Admin a join fetch a.answers where a.id = :id")
    Optional<Admin> findWithAnswerById(@Param("id") Long id);
}
