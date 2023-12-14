package io.oduck.api.domain.contact.service;

import io.oduck.api.domain.contact.dto.ContactReq.AnswerReq;
import io.oduck.api.domain.contact.dto.ContactReq.AnswerUpdateReq;
import io.oduck.api.domain.contact.dto.ContactReq.PostReq;
import io.oduck.api.domain.contact.dto.ContactRes.DetailRes;
import io.oduck.api.domain.contact.dto.ContactRes.MyInquiry;
import io.oduck.api.domain.contact.entity.FeedbackType;
import io.oduck.api.global.common.PageResponse;

public interface ContactService {
    void contact(Long memberId, PostReq request);

    PageResponse<MyInquiry> getAllByMemberId(Long memberId, int page, int size);

    DetailRes getByMemberId(Long inquiryId, Long memberId);

    boolean hasNotCheckedAnswer(Long id, Long memberId);

    void feedbackAnswer(Long id, Long memberId, FeedbackType helpful);

//    Page<?> getAll();
//
    void answer(Long id, Long adminId, AnswerReq request);

    void update(Long answerId, Long adminId, AnswerUpdateReq request);
}
