package io.oduck.api.domain.inquiry.service;

import io.oduck.api.domain.inquiry.dto.ContactReq.AnswerReq;
import io.oduck.api.domain.inquiry.dto.ContactReq.AnswerUpdateReq;
import io.oduck.api.domain.inquiry.dto.ContactReq.PostReq;
import io.oduck.api.domain.inquiry.dto.ContactRes.DetailRes;
import io.oduck.api.domain.inquiry.dto.ContactRes.MyInquiry;
import io.oduck.api.domain.inquiry.entity.FeedbackType;
import io.oduck.api.global.common.PageResponse;

public interface ContactService {
    void inquiry(Long memberId, PostReq request);

    PageResponse<MyInquiry> getAllByMemberId(Long memberId, int page, int size);

    DetailRes getByMemberId(Long inquiryId, Long memberId);

    boolean hasNotCheckedAnswer(Long id, Long memberId);

    void feedbackAnswer(Long id, Long memberId, FeedbackType helpful);

//    Page<?> getAll();
//
    void answer(Long id, Long adminId, AnswerReq request);

    void update(Long answerId, Long adminId, AnswerUpdateReq request);
}
