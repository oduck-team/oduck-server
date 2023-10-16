package io.oduck.api.domain.member.service;

import io.oduck.api.domain.member.dto.MemberReqDto.CreateReq;
import io.oduck.api.domain.member.dto.MemberResDto.MemberProfileRes;

public interface MemberService {
    // 로컬 회원가입 로직
    void signUpByLocal(CreateReq createReq);

    // 이름으로 회원 프로필 조회 로직
    MemberProfileRes getProfileByName(String name, Long memberId);

    // 회원 정보 수정 로직
    // void updateProfile(PatchReq body);
}
