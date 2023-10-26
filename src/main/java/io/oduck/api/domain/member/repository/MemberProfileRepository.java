package io.oduck.api.domain.member.repository;

import io.oduck.api.domain.member.entity.MemberProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberProfileRepository extends JpaRepository<MemberProfile,Long> {

    Optional<MemberProfile> findByMemberId(Long memberId);
    boolean existsByName(String name);
}
