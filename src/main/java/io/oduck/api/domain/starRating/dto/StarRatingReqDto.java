package io.oduck.api.domain.starRating.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class StarRatingReqDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAndPatchReq {
        @Min(value = 1, message = "score must be greater than or equal to 1")
        @Max(value = 10, message = "score must be less than or equal to 10")
        private int score;
    }

}
