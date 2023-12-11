package io.oduck.api.domain.inquiry.service;

import io.oduck.api.domain.inquiry.dto.ContactReq.AnswerUpdateReq;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AnswerUpdateHolder {
    private Long answerId;
    private String content;

    public static AnswerUpdateHolder from(Long answerId, AnswerUpdateReq request) {
        return new AnswerUpdateHolder(answerId, request.getContent());
    }
}
