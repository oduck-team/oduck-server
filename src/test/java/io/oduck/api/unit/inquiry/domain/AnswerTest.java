package io.oduck.api.unit.inquiry.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.oduck.api.domain.admin.entity.Admin;
import io.oduck.api.domain.contact.dto.ContactReq.AnswerReq;
import io.oduck.api.domain.contact.dto.ContactReq.AnswerUpdateReq;
import io.oduck.api.domain.contact.entity.Answer;
import io.oduck.api.domain.contact.entity.Contact;
import io.oduck.api.domain.contact.service.AnswerHolder;
import io.oduck.api.domain.contact.service.AnswerUpdateHolder;
import io.oduck.api.global.exception.BadRequestException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

public class AnswerTest {

    @Test
    void 관리자는_문의할_수_있다() {
        // given
        Contact contact = Contact.builder()
            .answered(false)
            .build();

        Admin admin = Admin.builder()
            .answers(new ArrayList<>())
            .build();

        AnswerReq request = new AnswerReq("문의");

        // when
        admin.answer(AnswerHolder.from(contact, request));

        // then
        assertThat(admin.getAnswers().get(0).getContact().isAnswered()).isTrue();
        assertThat(admin.getAnswers().get(0).getContent()).isEqualTo("문의");
    }

    @Test
    void 관리자는_답변을_수정할_수있다() {
        // given
        Contact contact = Contact.builder()
            .answered(false)
            .build();

        Admin admin = Admin.builder()
            .answers(new ArrayList<>())
            .build();

        AnswerReq request = new AnswerReq("문의");
        admin.answer(AnswerHolder.from(contact, request));

        // when
        admin.updateAnswer(AnswerUpdateHolder.from(null, new AnswerUpdateReq("문의 수정")));

        // then
        assertThat(admin.getAnswers().get(0).getContent()).isEqualTo("문의 수정");
    }

    @Test
    void 관리자는_확인한_답변을_수정할_수_없다() {
        // given
        Contact contact = Contact.builder()
            .answered(false)
            .build();

        Admin admin = Admin.builder()
            .answers(new ArrayList<>())
            .build();

        AnswerReq request = new AnswerReq("문의");
        admin.answer(AnswerHolder.from(contact, request));

        Answer answer = admin.getAnswers().get(0);
        answer.check();

        // when
        // then
        assertThatThrownBy(() -> admin.updateAnswer(AnswerUpdateHolder.from(null, new AnswerUpdateReq("문의 수정"))))
            .isInstanceOf(BadRequestException.class)
            .hasMessageContaining("이미 고객이 확인한 문의입니다.");
    }
}
