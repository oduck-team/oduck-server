package io.oduck.api.domain.reviewLike.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortReviewLikeReqDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortReviewLikeReq{
        Long shortReviewId;
    }

}