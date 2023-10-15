package io.oduck.api.global.initializer;

import io.oduck.api.domain.member.repository.MemberRepository;
import io.oduck.api.global.stub.MemberStub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

@Component
@ActiveProfiles("test")
public class MemberInitializer implements ApplicationRunner {

    @Autowired
    MemberRepository memberRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        MemberStub memberStub = new MemberStub();
        memberRepository.saveAll(memberStub.getMembers());
    }
}
