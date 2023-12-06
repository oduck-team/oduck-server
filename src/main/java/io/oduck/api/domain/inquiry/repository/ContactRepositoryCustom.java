package io.oduck.api.domain.inquiry.repository;

import io.oduck.api.domain.inquiry.dto.ContactRes.MyInquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactRepositoryCustom {

    Page<MyInquiry> getAllByMemberId(Long memberId, Pageable pageable);

    boolean existsByIdAndCheck(Long id, Boolean check);
}
