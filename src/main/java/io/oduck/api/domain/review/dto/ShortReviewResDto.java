package io.oduck.api.domain.review.dto;

import io.oduck.api.domain.anime.entity.Anime;
import io.oduck.api.domain.member.entity.MemberProfile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShortReviewResDto {
    private ShortReview shortReview;
//    private Anime anime;
//    private MemberProfile member;

    @Getter
    @Builder
    public static class ShortReview{
        private Long animeId;
        private String title;
        private String content;
        private boolean hasSpoiler;
        private int score;
        private int shortReviewLikeCount;
        private MemberProfile member;
        //private List<ShortReviewLike> shortReviewLikes;

    }

//    @Getter
//    @Builder
//    public static class Anime{
//        private Long animeId;
//        private String title;
//    }

    @Builder
    @Getter
    public static class MemberProfile{
        private String name;
        private String thumbnail;
    }
}