package io.oduck.api.domain.inquiry.service;

import io.oduck.api.domain.inquiry.dto.ContactReq.AnswerReq;
import io.oduck.api.domain.inquiry.entity.Contact;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AnswerHolder {
    private Contact contact;
    private AnswerReq request;
    public static AnswerHolder from(Contact contact, AnswerReq request) {
        return new AnswerHolder(contact, request);
    }
}
