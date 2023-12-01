package io.oduck.api.domain.inquiry.service;

import io.oduck.api.domain.inquiry.dto.InquiryReq.PostReq;
import io.oduck.api.domain.inquiry.dto.InquiryRes.DetailRes;
import io.oduck.api.domain.inquiry.dto.InquiryRes.MyInquiry;
import io.oduck.api.domain.inquiry.entity.FeedbackType;
import io.oduck.api.global.common.PageResponse;

public interface InquiryService {
    void inquiry(Long memberId, PostReq request);

    PageResponse<MyInquiry> getAllByMemberId(Long memberId, int page, int size);

    DetailRes getByMemberId(Long inquiryId, Long memberId);

    boolean hasNotCheckedAnswer(Long id, Long memberId);

    void feedbackAnswer(Long id, Long memberId, FeedbackType helpful);

//    Page<?> getAll();
//
//    void answer();
//
//    void update(Long id);
}
