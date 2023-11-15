package io.oduck.api.domain.member.repository;

import io.oduck.api.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long>, MemberRepositoryCustom{
    Optional<Member> findByIdAndDeletedAtIsNull(Long id);
}
