package io.oduck.api.global.initializer;

import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.entity.MemberProfile;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.security.auth.entity.AuthLocal;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
public class TestDataInitializer {

    private final MemberRepository memberRepository;

    @Autowired
    public TestDataInitializer(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @PostConstruct
    public void saveTestMember() {
        // TODO: Test Data들 Helper로 분리하기
        Member member = Member.builder()
            .loginType(LoginType.LOCAL)
            .role(Role.MEMBER)
            .build();

        AuthLocal authLocal = AuthLocal.builder()
            .email("bob@gmail.com")
            .password("{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i")
            .build();

        MemberProfile memberProfile = MemberProfile.builder()
            .name("admin")
            .info("bob info")
            .thumbnail("bob thumbnail")
            .point(0L)
            .build();

        member.setAuthLocal(authLocal);
        member.setMemberProfile(memberProfile);

        memberRepository.save(member);
    }
}
