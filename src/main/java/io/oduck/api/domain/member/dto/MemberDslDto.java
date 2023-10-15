package io.oduck.api.domain.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: VO로 변경 할지 논의 필요
public class MemberDslDto {
    @Getter
    @NoArgsConstructor
    public static class ProfileWithoutActivity {
        private Long memberId;
        private String name;
        private String description;
        private String thumbnail;
        private String backgroundImage;
        private Long point;

        @Builder
        public ProfileWithoutActivity(Long memberId, String name, String description, String thumbnail,
            String backgroundImage, Long point) {
            this.memberId = memberId;
            this.name = name;
            this.description = description;
            this.thumbnail = thumbnail;
            this.backgroundImage = backgroundImage;
            this.point = point;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class MemberActivity {
        private Long reviews;
        private Long likes;

        @Builder
        public MemberActivity(Long reviews, Long likes) {
            this.reviews = reviews;
            this.likes = likes;
        }
    }
}
