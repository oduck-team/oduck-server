package io.oduck.api.domain.inquiry.service;

import io.oduck.api.domain.inquiry.entity.Inquiry;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.global.exception.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class InquiryPolicy {

    public void isAccessOwnInquiry(Inquiry inquiry, Member member) {
        if(!isOwnInquiry(inquiry, member)) {
            throw new ForbiddenException("not has permission");
        }
        inquiry.getMember().getId();
    }

    private boolean isOwnInquiry(Inquiry inquiry, Member member) {
        return inquiry.getMember().getId().equals(member.getId());
    }
}