package io.oduck.api.domain.contact.service;

import io.oduck.api.domain.contact.entity.Contact;
import io.oduck.api.domain.member.entity.Member;
import io.oduck.api.global.exception.ForbiddenException;
import org.springframework.stereotype.Component;

@Component
public class ContactPolicy {

    public void isAccessOwnInquiry(Contact contact, Member member) {
        if(!isOwnInquiry(contact, member)) {
            throw new ForbiddenException("not has permission");
        }
        contact.getCustomer().getId();
    }

    private boolean isOwnInquiry(Contact contact, Member member) {
        return contact.getCustomer().getId().equals(member.getId());
    }
}