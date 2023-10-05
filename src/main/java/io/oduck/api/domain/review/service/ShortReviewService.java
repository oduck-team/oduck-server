package io.oduck.api.domain.review.service;

import io.oduck.api.domain.review.dto.ShortReviewResDto;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReview;

public interface ShortReviewService {

    //애니 조회
    ShortReviewResDto getShortReviews(Long anime);

}