package io.oduck.api.domain.review.service;

import io.oduck.api.domain.review.dto.ShortReviewResDto;

public interface ShortReviewService {

    //애니 짧은 리뷰 조회
    ShortReviewResDto getShortReviews(Long anime);
}