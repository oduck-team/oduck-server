package io.oduck.api.domain.inquiry.repository;

import io.oduck.api.domain.inquiry.entity.Inquiry;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InquiryRepository extends JpaRepository<Inquiry, Long>, InquiryRepositoryCustom {

    @Query("select i from Inquiry i join fetch i.member where i.id = :id")
    Optional<Inquiry> findWithMemberById(@Param("id") Long id);

    boolean existsByIdAndCheck(Long id, boolean isCheck);
}
