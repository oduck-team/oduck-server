package io.oduck.api.unit.inquiry.domain;


import static org.assertj.core.api.Assertions.assertThat;

import io.oduck.api.domain.inquiry.dto.CheckAnswerRequest;
import io.oduck.api.domain.inquiry.dto.InquiryFeedback;
import io.oduck.api.domain.inquiry.dto.InquiryReq.PostReq;
import io.oduck.api.domain.inquiry.dto.InquiryRequestHolder;
import io.oduck.api.domain.inquiry.entity.FeedbackType;
import io.oduck.api.domain.inquiry.entity.Inquiry;
import io.oduck.api.domain.inquiry.entity.InquiryAnswer;
import io.oduck.api.domain.inquiry.entity.InquiryType;
import io.oduck.api.domain.member.entity.Member;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class InquiryTest {

    @Test
    void 회원은_문의_할_수_있다() {
        //given
        Member target = Member.builder()
            .id(1L)
            .inquiries(new ArrayList<>())
            .build();

        PostReq postReq = new PostReq(InquiryType.ADD_REQUEST, "이거 왜 안 됨?", "왜");

        //when
        target.inquiry(InquiryRequestHolder.from(postReq, target));

        //then
        assertThat(target.getInquiries().size()).isEqualTo(1);
        assertThat(target.getInquiries().get(0).getTitle()).isEqualTo("이거 왜 안 됨?");
        assertThat(target.getInquiries().get(0).getContent()).isEqualTo("왜");
    }

    @Test
    void 회원은_운영진의_답변을_확인할_수_있다() {
        //given
        Member target = Member.builder()
            .id(1L)
            .inquiries(new ArrayList<>())
            .build();

        PostReq postReq = new PostReq(InquiryType.ADD_REQUEST, "이거 왜 안 됨?", "왜");

        target.inquiry(InquiryRequestHolder.from(postReq, target));
        Inquiry inquiry = target.getInquiries().get(0);

        //when
        target.checkAnswer(CheckAnswerRequest.from(inquiry.getId()));

        //then
        assertThat(inquiry.isCheck()).isTrue();
    }

    @Test
    void 회원은_운영진의_답변에_피드백_할_수_있다() {
        //given
        InquiryAnswer answer = InquiryAnswer.builder()
            .helpful(FeedbackType.NOT_SELECT)
            .build();

        Inquiry inquiry = Inquiry.builder()
            .inquiryAnswer(answer)
            .build();

        List<Inquiry> inquiries = new ArrayList<>();
        inquiries.add(inquiry);

        Member target = Member.builder()
            .id(1L)
            .inquiries(inquiries)
            .build();

        //when
        target.feedbackInquiry(InquiryFeedback.from(null, FeedbackType.HELPFUL));

        //then
        assertThat(target.getInquiries().get(0).getInquiryAnswer().getHelpful()).isSameAs(FeedbackType.HELPFUL);
    }
}
