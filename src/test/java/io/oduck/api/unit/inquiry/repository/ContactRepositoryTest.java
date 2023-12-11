package io.oduck.api.unit.inquiry.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.oduck.api.domain.contact.dto.ContactReq.PostReq;
import io.oduck.api.domain.contact.dto.ContactRequestHolder;
import io.oduck.api.domain.contact.entity.InquiryType;
import io.oduck.api.domain.contact.repository.ContactRepository;
import io.oduck.api.domain.member.entity.Member;
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
}
