package io.oduck.api.domain.inquiry.repository;

import io.oduck.api.domain.inquiry.dto.InquiryRes.MyInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InquiryRepositoryCustom {
    Page<MyInquiry> getAllByMemberId(Long memberId, Pageable pageable);
}
