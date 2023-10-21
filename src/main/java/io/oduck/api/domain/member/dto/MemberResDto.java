package io.oduck.api.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResDto {
    @Getter
    @NoArgsConstructor
    public static class MemberProfileRes {
        private boolean isMine;
        private Long memberId;
        private String name;
        private String description;
        private String thumbnail;
        private String backgroundImage;
        private Activity activity;

        @Builder
        public MemberProfileRes(boolean isMine, Long memberId, String name, String description, String thumbnail,
            String backgroundImage, Activity activity) {
            this.isMine = isMine;
            this.memberId = memberId;
            this.name = name;
            this.description = description;
            this.thumbnail = thumbnail;
            this.backgroundImage = backgroundImage;
            this.activity = activity;
        }

        public boolean getIsMine() {
            return isMine;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Activity {
        private long reviews;
        private long bookmarks;
        private long likes;
        private long point;

        @Builder
        public Activity(long reviews, long bookmarks, long likes, long point) {
            this.reviews = reviews;
            this.bookmarks = bookmarks;
            this.likes = likes;
            this.point = point;
        }
    }
}
