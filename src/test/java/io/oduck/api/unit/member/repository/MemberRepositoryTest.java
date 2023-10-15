package io.oduck.api.unit.member.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.oduck.api.domain.member.dto.MemberDslDto.ProfileWithoutActivity;
import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.entity.MemberProfile;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.domain.member.repository.MemberProfileRepository;
import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.config.QueryDslTestConfig;
import io.oduck.api.global.initializer.TestDataInitializer;
import io.oduck.api.global.security.auth.entity.AuthLocal;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
// QueryDSL repository 테스트를 위한 설정 불러오기
@Import(QueryDslTestConfig.class)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberProfileRepository memberProfileRepository;

    // 테스트 데이터 초기화
    // SpringBootTest는 기본적으로 테스트마다 새로운 ApplicationContext를 생성하는데,
    // 유닛 테스트에서는 SpringBootTest를 사용하지 않고, DataJpaTest를 사용하기 때문에
    // 테스트 데이터를 초기화하기 위해 @BeforeAll을 사용해야 함.
    @BeforeAll
    void setUp() {
        TestDataInitializer testDataInitializer = new TestDataInitializer(memberRepository);
        testDataInitializer.saveTestMember();
    }

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
