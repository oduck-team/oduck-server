package io.oduck.api.domain.inquiry.dto;

import io.oduck.api.domain.inquiry.entity.Inquiry;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class InquiryRes {

    @Getter
    @AllArgsConstructor
    public static class MyInquiry {
        private Long id;
        private String title;
        private LocalDate localDate;
        private boolean answer;

        public MyInquiry(Long id, String title, LocalDateTime localDateTime, boolean answer) {
            this.id = id;
            this.title = title;
            this.localDate = localDateTime.toLocalDate();
            this.answer = answer;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class DetailRes {
        private String title;
        private LocalDate localDate;
        private boolean answer;

        public static DetailRes from(Inquiry inquiry) {
            return new DetailRes(inquiry.getTitle(), inquiry.getCreatedAt().toLocalDate(), inquiry.isAnswer());
        }
    }
}

