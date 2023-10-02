package io.oduck.api.domain.member.service;

import io.oduck.api.domain.member.dto.MemberResDto.Activity;
import io.oduck.api.domain.member.dto.MemberResDto.MemberProfileRes;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceStub implements MemberService{


    @Override
    public MemberProfileRes getProfileByName(String name) {

        Activity activity = Activity.builder()
            .reviews(0)
            .threads(0)
            .likes(0)
            .build();

        MemberProfileRes memberProfile = MemberProfileRes.builder()
            .isMine(true)
            .name(name)
            .description("자기소개")
            .thumbnail("썸네일")
            .backgroundImage("배경 이미지")
            .activity(activity)
            .point(0)
            .build();

        return memberProfile;
    }
}
