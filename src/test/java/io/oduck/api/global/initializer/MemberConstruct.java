package io.oduck.api.global.initializer;

import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.stub.MemberStub;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
public class MemberConstruct {

    @Autowired
    MemberRepository memberRepository;

    @PostConstruct
    public void run() {
        MemberStub memberStub = new MemberStub();
        memberRepository.saveAll(memberStub.getMembers());
    }
}
