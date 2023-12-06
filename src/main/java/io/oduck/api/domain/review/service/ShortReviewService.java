package io.oduck.api.domain.review.service;

import io.oduck.api.domain.review.dto.ShortReviewDslDto.ShortReviewDslWithTitle;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.ShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.Sort;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.SortForProfile;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReviewCountRes;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReviewRes;
import io.oduck.api.domain.review.dto.ShortReviewResDto.ShortReviewResWithTitle;
import io.oduck.api.global.common.OrderDirection;
import io.oduck.api.global.common.SliceResponse;

public interface ShortReviewService {

    //애니 리뷰 작성
    void save(Long memberId, ShortReviewReq shortReviewReq);

    //애니 짧은 리뷰 조회
    SliceResponse<ShortReviewRes> getShortReviews(Long animeId, String cursor, Sort sort, OrderDirection order, int size);
    ShortReviewCountRes getShortReviewCountByMemberId(Long memberId);

    SliceResponse<ShortReviewResWithTitle> getShortReviewsByMemberId(Long memberId, String cursor, SortForProfile sort, OrderDirection order, int size);

    //애니 리뷰 수정
    void update(Long memberId, Long reviewId, ShortReviewReq req);

    //애니 리뷰 삭제

}