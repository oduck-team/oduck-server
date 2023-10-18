package io.oduck.api.global.utils;

import io.oduck.api.domain.review.dto.ShortReviewReqDto.PatchShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PostShortReviewReq;

public class ShortReviewTestUtils {

    public static PostShortReviewReq createPostShoreReviewReq(){
        return new PostShortReviewReq(
            getMemberId(),getAnimeId(),
            isHasSpoiler(),getContent()
        );
    }

    public static PatchShortReviewReq createPatchShortReview(){
        return new PatchShortReviewReq(
            isHasSpoiler(),getContent()
        );
    }

    public static Long getMemberId(){
        return 1L;
    }

    public static Long getAnimeId(){
        return 1L;
    }
    public static boolean isHasSpoiler(){
        return false;
    }
    public static String getContent(){
        return "최고의 반전의 반전의 반전";
    }
}
