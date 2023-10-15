package io.oduck.api.domain.member.repository;

import io.oduck.api.domain.member.dto.MemberDslDto.MemberActivity;
import io.oduck.api.domain.member.dto.MemberDslDto.ProfileWithoutActivity;

public interface MemberRepositoryCustom {
    ProfileWithoutActivity selectProfileByName(String name);

    MemberActivity selectActivityByMemberId(Long id);
}
