package io.oduck.api.domain.inquiry.dto;

import io.oduck.api.domain.inquiry.dto.InquiryReq.PostReq;
import io.oduck.api.domain.inquiry.entity.Inquiry;
import io.oduck.api.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class InquiryRequestHolder {
    private Inquiry inquiry;

    public static InquiryRequestHolder from(PostReq request, Member member) {
        Inquiry inquiry = Inquiry.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .type(request.getType())
            .answer(false)
            .check(false)
            .member(member)
            .build();
        return new InquiryRequestHolder(inquiry);
    }
}
