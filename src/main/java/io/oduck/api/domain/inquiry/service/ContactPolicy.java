package io.oduck.api.domain.inquiry.service;

import io.oduck.api.domain.inquiry.entity.Contact;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.global.exception.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class ContactPolicy {

    public void isAccessOwnInquiry(Contact contact, Member member) {
        if(!isOwnInquiry(contact, member)) {
            throw new ForbiddenException("not has permission");
        }
        contact.getMember().getId();
    }

    private boolean isOwnInquiry(Contact contact, Member member) {
        return contact.getMember().getId().equals(member.getId());
    }
}