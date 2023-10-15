package io.oduck.api.global.initializer;

import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.stub.MemberStub;
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
        // 회원 데이터 초기화
        MemberStub memberstub = new MemberStub();
        memberstub.init();
        memberRepository.saveAll(memberstub.getMembers());
    }
}
