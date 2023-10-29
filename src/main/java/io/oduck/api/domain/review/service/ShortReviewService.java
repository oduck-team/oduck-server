package io.oduck.api.domain.review.service;

import io.oduck.api.domain.review.dto.ShortReviewReqDto.PatchShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PostShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.Sort;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReviewRes;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;

public interface ShortReviewService {

    //애니 짧은 리뷰 조회
    SliceResponse<ShortReviewRes> getShortReviews(Long animeId, String cursor, Sort sort, OrderDirection order, int size);

    //애니 리뷰 작성
    void save(Long memberId, PostShortReviewReq shortReviewReq);

    //애니 리뷰 수정
    void update(Long memberId, Long reviewId, PatchShortReviewReq req);

    //애니 리뷰 삭제

}