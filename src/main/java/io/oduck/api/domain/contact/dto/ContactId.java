package io.oduck.api.domain.contact.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactId {
    private Long contactId;

    public static ContactId from(Long inquiryId) {
        return new ContactId(inquiryId);
    }
}
