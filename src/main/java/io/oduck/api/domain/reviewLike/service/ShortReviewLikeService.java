package io.oduck.api.domain.reviewLike.service;


import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeReqDto.ShortReviewLikeReq;
import io.oduck.api.domain.reviewLike.dto.ShortReviewLikeResDto.HasLikeRes;

public interface ShortReviewLikeService {

    //리뷰 좋아요
    Boolean postLike(Long memberId, ShortReviewLikeReq likeRes);

    //리뷰 좋아요 유무
    HasLikeRes checkReviewLike(Long likeId, Long memberId);

}
