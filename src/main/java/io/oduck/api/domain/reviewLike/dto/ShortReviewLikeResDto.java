package io.oduck.api.domain.reviewLike.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortReviewLikeResDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IsLikeRes{
        private Boolean isLike;
    }
}