package io.oduck.api.unit.inquiry.domain;


import static org.assertj.core.api.Assertions.assertThat;

import io.oduck.api.domain.contact.dto.ContactId;
import io.oduck.api.domain.contact.dto.AnswerFeedback;
import io.oduck.api.domain.contact.dto.ContactReq.PostReq;
import io.oduck.api.domain.contact.dto.ContactRequestHolder;
import io.oduck.api.domain.contact.entity.Answer;
import io.oduck.api.domain.contact.entity.Contact;
import io.oduck.api.domain.contact.entity.FeedbackType;
import io.oduck.api.domain.contact.entity.InquiryType;
import io.oduck.api.domain.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ContactTest {

    @Test
    void 회원은_문의_할_수_있다() {
        //given
        Member target = Member.builder()
            .id(1L)
            .contacts(new ArrayList<>())
            .build();

        PostReq postReq = new PostReq(InquiryType.ADD_REQUEST, "이거 왜 안 됨?", "왜");

        //when
        target.inquiry(ContactRequestHolder.from(postReq, target));

        //then
        assertThat(target.getContacts().size()).isEqualTo(1);
        assertThat(target.getContacts().get(0).getTitle()).isEqualTo("이거 왜 안 됨?");
        assertThat(target.getContacts().get(0).getContent()).isEqualTo("왜");
    }

    @Test
    void 회원은_운영진의_답변을_확인할_수_있다() {
        //given
        Member target = Member.builder()
            .id(1L)
            .contacts(new ArrayList<>())
            .build();

        Answer answer = Answer.builder()
            .check(false)
            .build();

        Contact contact = Contact.builder()
            .id(1L)
            .answer(answer)
            .build();

        target.getContacts().add(contact);

        //when
        target.checkAnswer(ContactId.from(1L));

        //then
        assertThat(contact.getAnswer().isCheck()).isTrue();
    }

    @Test
    void 회원은_운영진의_답변에_피드백_할_수_있다() {
        //given
        Answer answer = Answer.builder()
            .helpful(FeedbackType.NONE)
            .build();

        Contact contact = Contact.builder()
            .answer(answer)
            .build();

        List<Contact> inquiries = new ArrayList<>();
        inquiries.add(contact);

        Member target = Member.builder()
            .id(1L)
            .contacts(inquiries)
            .build();

        //when
        target.feedbackAnswer(AnswerFeedback.from(null, FeedbackType.HELPFUL));

        //then
        assertThat(target.getContacts().get(0).getAnswer().getHelpful()).isSameAs(FeedbackType.HELPFUL);
    }
}
