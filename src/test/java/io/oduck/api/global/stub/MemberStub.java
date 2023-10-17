package io.oduck.api.global.stub;

import io.oduck.api.domain.member.entity.LoginType;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.entity.MemberProfile;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.global.security.auth.entity.AuthLocal;
import java.util.ArrayList;
import java.util.List;

public class MemberStub {
    List<Member> members = new ArrayList<>();

    public List<Member> getMembers() {
        return members;
    }

    public MemberStub() {
        // Member1
        Member member1 = Member.builder()
            .loginType(LoginType.LOCAL)
            .role(Role.MEMBER)
            .build();

        AuthLocal authLocal1 = AuthLocal.builder()
            .email("oduckdmin@gmail.com")
            .password("{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i")
            .build();

        MemberProfile memberProfile1 = MemberProfile.builder()
            .name("admin")
            .info("admin info")
            .thumbnail("http://thumbnail.com")
            .point(0L)
            .build();

        member1.relateAuthLocal(authLocal1);
        member1.relateMemberProfile(memberProfile1);

        // member2
        Member member2 = Member.builder()
            .loginType(LoginType.LOCAL)
            .role(Role.MEMBER)
            .build();

        AuthLocal authLocal2 = AuthLocal.builder()
            .email("john@gmail.com")
            .password("{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i")
            .build();

        MemberProfile memberProfile2 = MemberProfile.builder()
            .name("john")
            .info("john info")
            .thumbnail("http://thumbnail.com")
            .point(0L)
            .build();

        member2.relateAuthLocal(authLocal2);
        member2.relateMemberProfile(memberProfile2);

        // member3
        Member member3 = Member.builder()
            .loginType(LoginType.LOCAL)
            .role(Role.MEMBER)
            .build();

        AuthLocal authLocal3 = AuthLocal.builder()
            .email("david@gmail.com")
            .password("{bcrypt}$2a$10$C0G6uQz.MzfsSH7BZFRBz.MPBmFSV2zAloqqBIwaUpxnmMgCQK..i")
            .build();

        MemberProfile memberProfile3 = MemberProfile.builder()
            .name("david")
            .info("david info")
            .thumbnail("http://thumbnail.com")
            .point(0L)
            .build();

        member3.relateAuthLocal(authLocal3);
        member3.relateMemberProfile(memberProfile3);

        members.addAll(List.of(member1, member2, member3));
    }

    public Member getMember() {
        return members.get(0);
    }
}
