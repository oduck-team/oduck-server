package io.oduck.api.domain.inquiry.repository;

import io.oduck.api.domain.inquiry.entity.Contact;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Long>, ContactRepositoryCustom {

    @Query("select c from Contact c join fetch c.member where c.id = :id")
    Optional<Contact> findWithMemberById(@Param("id") Long id);
}
