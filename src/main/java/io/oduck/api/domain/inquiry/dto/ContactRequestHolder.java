package io.oduck.api.domain.inquiry.dto;

import io.oduck.api.domain.inquiry.dto.ContactReq.PostReq;
import io.oduck.api.domain.inquiry.entity.Contact;
import io.oduck.api.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ContactRequestHolder {
    private Contact contact;

    public static ContactRequestHolder from(PostReq request, Member member) {
        Contact contact = Contact.builder()
            .title(request.getTitle())
            .content(request.getContent())
            .type(request.getType())
            .answered(false)
            .member(member)
            .build();
        return new ContactRequestHolder(contact);
    }
}
