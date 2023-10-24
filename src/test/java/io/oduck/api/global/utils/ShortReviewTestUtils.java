package io.oduck.api.global.utils;

import io.oduck.api.domain.review.dto.ShortReviewReqDto.PatchShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PostShortReviewReq;

public class ShortReviewTestUtils {

    public static PostShortReviewReq createPostShoreReviewReq(){
        return new PostShortReviewReq(
            getAnimeId(),getName(),
            isHasSpoiler(),getContent()
        );
    }

    public static PatchShortReviewReq createPatchShortReview(){
        return new PatchShortReviewReq(
            getAnimeId(), getName(), isHasSpoiler(),getContent()
        );
    }

    public static String getName(){return "회원이름";}

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
