package io.oduck.api.unit.inquiry.repository;

import static org.assertj.core.api.Assertions.*;

import io.oduck.api.domain.admin.entity.Admin;
import io.oduck.api.domain.admin.repository.AdminRepository;
import io.oduck.api.domain.inquiry.dto.ContactReq.AnswerReq;
import io.oduck.api.domain.inquiry.dto.ContactReq.PostReq;
import io.oduck.api.domain.inquiry.dto.ContactRequestHolder;
import io.oduck.api.domain.inquiry.entity.Contact;
import io.oduck.api.domain.inquiry.entity.Answer;
import io.oduck.api.domain.inquiry.entity.InquiryType;
import io.oduck.api.domain.inquiry.repository.ContactRepository;
import io.oduck.api.domain.inquiry.service.AnswerHolder;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.domain.member.entity.Role;
import io.oduck.api.domain.member.repository.MemberRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class ContactRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void 문의시_문의가_저장된다() {
        //given
        Member member = Member.builder()
            .contacts(new ArrayList<>())
            .build();
        Member target = memberRepository.save(member);

        PostReq postReq = new PostReq(InquiryType.ADD_REQUEST, "이거 왜 안 됨?", "왜");

        //when
        target.inquiry(ContactRequestHolder.from(postReq, target));

        //then
        assertThat(target.getContacts().isEmpty()).isFalse();
        assertThat(target.getContacts().size()).isEqualTo(1L);
        assertThat(target.getContacts().get(0).getTitle()).isEqualTo("이거 왜 안 됨?");
        assertThat(target.getContacts().get(0).getContent()).isEqualTo("왜");
        assertThat(target.getContacts().get(0).getType()).isEqualTo(InquiryType.ADD_REQUEST);
    }

    @Test
    void 문의를_아이디와_체크여부로_조회할_수_있다() {
        //given
        Member member = Member.builder()
            .contacts(new ArrayList<>())
            .build();
        memberRepository.save(member);

        PostReq postReq = new PostReq(InquiryType.ADD_REQUEST, "이거 왜 안 됨?", "왜");
        member.inquiry(ContactRequestHolder.from(postReq, member));
        Contact contact = member.getContacts().get(0);

        Member admin = Member.builder()
            .role(Role.ADMIN)
            .build();
        memberRepository.save(admin);

        Admin target = adminRepository.findById(admin.getId()).get();

        AnswerReq request = new AnswerReq(contact.getId(), "죄송요 ㅎㅎ;");

        target.answerInquiry(AnswerHolder.from(contact, request));
        Answer answer = target.getAnswer();

        //when
        boolean exists = contactRepository.existsByIdAndCheck(answer.getId(), false);

        //then
        assertThat(exists).isFalse();
    }
}
