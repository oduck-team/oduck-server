package io.oduck.api.domain.review.service;

import io.oduck.api.domain.review.dto.ShortReviewReqDto;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PostShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewResDto;
import io.oduck.api.domain.review.entity.ShortReview;

public interface ShortReviewService {

    //애니 짧은 리뷰 조회
    ShortReviewResDto getShortReviews(Long anime);
}