package io.oduck.api.domain.member.service;

import static io.oduck.api.global.utils.NameGenerator.generateNickname;

import io.oduck.api.domain.member.dto.MemberReqDto.CreateReq;
import io.oduck.api.domain.member.dto.MemberResDto.Activity;
import io.oduck.api.domain.member.dto.MemberResDto.MemberProfileRes;
import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.entity.MemberProfile;
import io.oduck.api.domain.member.repository.MemberProfileRepository;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.exception.BadRequestException;
import io.oduck.api.global.exception.ConflictException;
import io.oduck.api.global.security.auth.entity.AuthLocal;
import io.oduck.api.global.security.auth.repository.AuthLocalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceStub implements MemberService {
    private final PasswordEncoder passwordEncoder;
    private final AuthLocalRepository authLocalRepository;
    private final MemberRepository memberRepository;
    private final MemberProfileRepository memberProfileRepository;

    @Transactional
    @Override
    public void signUpByLocal(CreateReq createReq) {
        String email = createReq.getEmail();
        boolean isExistMember = authLocalRepository.existsByEmail(email);

        if (isExistMember) {
            throw new ConflictException("Email");
        }

        try {
            String encryptedPassword = passwordEncoder.encode(createReq.getPassword());

            AuthLocal authLocal = AuthLocal.builder()
                .email(createReq.getEmail())
                .password(encryptedPassword)
                .build();

            MemberProfile memberProfile = MemberProfile.builder()
                .name(generateNickname())
                .build();

            Member member = Member.builder()
                .loginType(LoginType.LOCAL)
                .build();

            member.setAuthLocal(authLocal);
            member.setMemberProfile(memberProfile);

            Member savedMember = memberRepository.save(member);
            log.info("Member Created! {}", savedMember.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadRequestException("Try Again Plz.");
        }
    }

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
            .point(0L)
            .build();

        return memberProfile;
    }
}
