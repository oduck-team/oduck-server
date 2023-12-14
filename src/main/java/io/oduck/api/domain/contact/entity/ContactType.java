package io.oduck.api.domain.contact.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ContactType {
    ADD_REQUEST("기능 추가 건의"),
    BUG_REPORT("버그 신고"),
    ETC_REQUEST("기타 문의"),
    ;

    String description;
}
