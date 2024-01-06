package io.oduck.api.global.notification.dto;

import io.oduck.api.global.webHook.DiscordWebhook.EmbedObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Message {
    private EmbedObject content;

    public static Message from(EmbedObject content) {
        return Message.builder()
            .content(content)
            .build();
    }
}
