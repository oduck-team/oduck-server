package io.oduck.api.unit.member.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.oduck.api.domain.anime.repository.AnimeRepository;
import io.oduck.api.domain.member.dto.MemberDslDto.ProfileWithoutActivity;
import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.entity.MemberProfile;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.domain.review.repository.ShortReviewRepository;
import io.oduck.api.global.security.auth.entity.AuthLocal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AnimeRepository animeRepository;
    @Autowired
    ShortReviewRepository shortReviewRepository;

    @DisplayName("회원 저장")
    @Test
    void saveMember() {
        // given
        Member member = Member.builder()
            .role(Role.MEMBER)
            .loginType(LoginType.LOCAL)
            .build();

        AuthLocal authLocal = AuthLocal.builder()
            .email("bob@gmail.com")
            .password("{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i")
            .build();

        MemberProfile memberProfile = MemberProfile.builder()
            .name("bob")
            .info("bob info")
            .thumbnail("bob thumbnail")
            .point(0L)
            .build();

        member.setAuthLocal(authLocal);
        member.setMemberProfile(memberProfile);

        // when
        Member savedMember = memberRepository.save(member);

        // then
        assertNotNull(savedMember);                                             // savedMember가 null이면 안 됨.
        assertEquals(member.getId(), savedMember.getId());
        assertEquals(member.getRole(), savedMember.getRole());
        assertEquals(member.getAuthLocal().getEmail(), savedMember.getAuthLocal().getEmail());
        assertEquals(member.getMemberProfile().getName(), savedMember.getMemberProfile().getName());
    }

    @DisplayName("회원 이름으로 프로필 조회")
    @Test
    void selectProfileByName() {
        // given
        String name = "admin";

        // when
        ProfileWithoutActivity memberProfile = memberRepository.selectProfileByName(name);

        // then
        assertNotNull(memberProfile);
        assertEquals(name, memberProfile.getName());
    }
}
