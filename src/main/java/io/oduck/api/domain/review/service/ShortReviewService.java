package io.oduck.api.domain.review.service;

import io.oduck.api.domain.review.dto.ShortReviewReqDto.PatchShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PostShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewResDto;

public interface ShortReviewService {

    //애니 짧은 리뷰 조회
    ShortReviewResDto getShortReviews(Long anime);

    //애니 리뷰 작성
    void save(PostShortReviewReq shortReviewReq);

    //애니 리뷰 수정
    void update(Long reviewId, PatchShortReviewReq req);

    //애니 리뷰 삭제

}