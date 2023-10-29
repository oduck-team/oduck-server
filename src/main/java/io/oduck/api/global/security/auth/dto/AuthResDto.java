package io.oduck.api.global.security.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class AuthResDto {
    @Getter
    @NoArgsConstructor
    public static class Status {
        private Long memberId;
        private String name;
        private String description;
        private String thumbnail;
        private Long point;

        @Builder
        public Status(Long memberId, String name, String description, String thumbnail,
            Long point) {
            this.memberId = memberId;
            this.name = name;
            this.description = description;
            this.thumbnail = thumbnail;
            this.point = point;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class AdminStatus extends Status {
        private String role;

        public AdminStatus(Status status, String role) {
            super(status.getMemberId(), status.getName(), status.getDescription(), status.getThumbnail(), status.getPoint());
            this.role = role;
        }

        public static AdminStatusBuilder builder() {
            return new AdminStatusBuilder();
        }

        public static class AdminStatusBuilder extends StatusBuilder {
            private Status status;
            private String role;

            public AdminStatusBuilder status(Status status) {
                this.status = status;
                return this;
            }

            public AdminStatusBuilder role(String role) {
                this.role = role;
                return this;
            }

            public AdminStatus build() {
                return new AdminStatus(status, role);
            }
        }
    }
}
