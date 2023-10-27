package io.oduck.api.global.utils;

import static io.oduck.api.global.utils.AnimeTestUtils.createAnime;

import io.oduck.api.domain.review.dto.ShortReviewReqDto.PatchShortReviewReq;
import io.oduck.api.domain.review.dto.ShortReviewReqDto.PostShortReviewReq;
import io.oduck.api.domain.review.entity.ShortReview;
import io.oduck.api.global.stub.MemberStub;

public class ShortReviewTestUtils {

    public static PostShortReviewReq createPostShoreReviewReq(){
        return new PostShortReviewReq(
            getAnimeId(),getName(),
            isHasSpoiler(),getContent()
        );
    }

    public static PatchShortReviewReq createPatchShortReview(){
        return new PatchShortReviewReq(
            getAnimeId(), getName(), isHasSpoiler(),updateContent()
        );
    }

    public static String getName(){return "회원 이름";}

    public static Long getAnimeId(){
        return 1L;
    }
    public static boolean isHasSpoiler(){
        return false;
    }
    public static String getContent(){
        return "최고의 반전의 반전의 반전";
    }

    public static boolean updateHasSpoiler(){
        return true;
    }
    public static String updateContent(){
        return "멋진 결말 이였다 다시봐야지";
    }



    public static ShortReview createShortReview(){
        return ShortReview
                   .builder()
                   .member(new MemberStub().getMember())
                   .anime(createAnime())
                   .content(getContent())
                   .hasSpoiler(isHasSpoiler())
                   .build();
    }
}
