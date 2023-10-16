package io.oduck.api.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class MemberResDto {
    @Getter
    @NoArgsConstructor
    public static class MemberProfileRes {
        private boolean isMine;
        private String name;
        private String description;
        private String thumbnail;
        private String backgroundImage;
        private Activity activity;
        private Long point;

        @Builder
        public MemberProfileRes(boolean isMine, String name, String description, String thumbnail,
            String backgroundImage, Activity activity, Long point) {
            this.isMine = isMine;
            this.name = name;
            this.description = description;
            this.thumbnail = thumbnail;
            this.backgroundImage = backgroundImage;
            this.activity = activity;
            this.point = point;
        }

        public boolean getIsMine() {
            return isMine;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Activity {
        private long reviews;
        private long threads;
        private long likes;

        @Builder
        public Activity(long reviews, long threads, long likes) {
            this.reviews = reviews;
            this.threads = threads;
            this.likes = likes;
        }
    }
}
