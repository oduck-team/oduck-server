package io.oduck.api.domain.reviewLike.service;


import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeReqDto.ShortReviewLikeReq;
import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeResDto.IsLikeRes;

public interface ShortReviewLikeService {

    //리뷰 좋아요
    Boolean postLike(Long memberId, ShortReviewLikeReq likeRes);

    //리뷰 좋아요 유무
    IsLikeRes checkReviewLike(ShortReviewLikeReq req, Long memberId);

}
