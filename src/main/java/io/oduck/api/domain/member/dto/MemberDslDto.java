package io.oduck.api.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: VO로 변경 할지 논의 필요
public class MemberDslDto {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileWithoutActivity {
        private Long memberId;
        private String name;
        private String description;
        private String thumbnail;
        private String backgroundImage;
        private Long point;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberActivity {
        private Long reviews;
        private Long likes;
    }
}
