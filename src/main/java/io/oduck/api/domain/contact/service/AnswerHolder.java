package io.oduck.api.domain.contact.service;

import io.oduck.api.domain.contact.dto.ContactReq.AnswerReq;
import io.oduck.api.domain.contact.entity.Contact;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnswerHolder {
    private Contact contact;
    private String content;

    public static AnswerHolder from(Contact contact, AnswerReq request) {
        return new AnswerHolder(contact, request.getContent());
    }
}
