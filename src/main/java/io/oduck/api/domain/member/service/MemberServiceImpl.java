package io.oduck.api.domain.member.service;

import static io.oduck.api.global.utils.NameGenerator.generateNickname;

import io.oduck.api.domain.member.dto.MemberDslDto.ProfileWithoutActivity;
import io.oduck.api.domain.member.dto.MemberReqDto.CreateReq;
import io.oduck.api.domain.member.dto.MemberReqDto.PatchReq;
import io.oduck.api.domain.member.dto.MemberResDto.Activity;
import io.oduck.api.domain.member.dto.MemberResDto.MemberProfileRes;
import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.entity.MemberProfile;
import io.oduck.api.domain.member.repository.MemberProfileRepository;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.exception.BadRequestException;
import io.oduck.api.global.exception.ConflictException;
import io.oduck.api.global.exception.NotFoundException;
import io.oduck.api.global.security.auth.entity.AuthLocal;
import io.oduck.api.global.security.auth.repository.AuthLocalRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{
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

            member.relateAuthLocal(authLocal);
            member.relateMemberProfile(memberProfile);

            Member savedMember = memberRepository.save(member);
            log.info("Member Created! {}", savedMember.getId());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BadRequestException("Try Again Plz.");
        }
    }

    @Override
    public MemberProfileRes getProfileByName(String name, Long memberId) {
        ProfileWithoutActivity memberProfile = getProfileWithoutActivity(name);
        if (memberProfile == null) {
            throw new NotFoundException("Member");
        }
        Long reviewsCount = memberRepository.countReviewsByMemberId(memberProfile.getMemberId());
        Long bookmarksCount = memberRepository.countBookmarksByMemberId(memberProfile.getMemberId());
        Long likesCount = memberRepository.countLikesByMemberId(memberProfile.getMemberId());


        Activity activity = Activity.builder()
            .reviews(reviewsCount)
            .bookmarks(bookmarksCount)
            .likes(likesCount)
            .build();

        MemberProfileRes memberProfileRes = MemberProfileRes. builder()
            .isMine(memberProfile.getMemberId().equals(memberId))
            .name (memberProfile.getName())
            .description(memberProfile.getDescription())
            .thumbnail(memberProfile.getThumbnail())
            .backgroundImage (memberProfile.getBackgroundImage())
            .point(memberProfile.getPoint())
            .activity(activity)
            .build();

        return memberProfileRes;
    }

    @Override
    public void updateProfile(PatchReq body, Long memberId) {
        MemberProfile memberProfile = getProfileByMemberId(memberId);

        // Null 체크
        Optional
            .ofNullable(body.getName())
            .ifPresent(
                name -> {
                    // 이전 이름과 같은지 체크
                    if (memberProfile.getName().equals(name)) {
                        throw new BadRequestException("Same name as before.");
                    }

                    // 이름 중복 체크
                    checkDuplicatedName(name);

                    memberProfile.updateName(name);
                }
            );

        // Null 체크
        Optional
            .ofNullable(body.getDescription())
            .ifPresent(
                memberProfile::updateInfo
            );

        memberProfileRepository.save(memberProfile);
    }

    private MemberProfile getProfileByMemberId(Long memberId) {
        return memberProfileRepository.findByMemberId(memberId)
            .orElseThrow(
                () -> new NotFoundException("Member")
            );
    }

    private ProfileWithoutActivity getProfileWithoutActivity(String name) {
        return memberRepository.selectProfileByName(name)
            .orElseThrow(
                () -> new NotFoundException("Member")
            );
    }

    private void checkDuplicatedName(String name) {
        if (memberProfileRepository.existsByName(name)) {
            throw new ConflictException("Name");
        }
    }
}
