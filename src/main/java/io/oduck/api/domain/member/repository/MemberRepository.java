package io.oduck.api.domain.member.repository;

import io.oduck.api.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom{
    Optional<Member> findByIdAndDeletedAtIsNull(Long id);

    @Query("select m from Member m join fetch m.memberProfile p where m.id = :id")
    Optional<Member> findWithProfileById(@Param("id") Long id);
}
