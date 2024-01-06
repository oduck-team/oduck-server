package io.oduck.api.domain.contact.infra.event;

import io.oduck.api.domain.contact.dto.ContactReq.PostReq;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class ContactEvent {
    private String title;
    private String content;
    private String type;
    private String name;

    public static ContactEvent from(PostReq request, String nickname) {
        return ContactEvent.builder()
            .type(request.getType().getDescription())
            .title(request.getTitle())
            .content(request.getContent())
            .name(nickname)
            .build();
    }
}
