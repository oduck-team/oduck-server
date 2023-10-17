package io.oduck.api.domain.member.repository;

import io.oduck.api.domain.member.dto.MemberDslDto.ProfileWithoutActivity;
import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<ProfileWithoutActivity> selectProfileByName(String name);

    Long countReviewsByMemberId(Long id);
    Long countBookmarksByMemberId(Long id);

    Long countLikesByMemberId(Long id);
}
