package io.oduck.api.domain.member.service;

import io.oduck.api.domain.member.dto.MemberResDto.MemberProfileRes;

public interface MemberService {
    // 로컬 회원가입 로직
    // Member signUpByLocal(Member member);

    // 이름으로 회원 프로필 조회 로직
    MemberProfileRes getProfileByName(String name);

    // 회원 정보 수정 로직
    // void updateProfile(PatchReq body);
}
