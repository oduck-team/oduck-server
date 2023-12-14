package io.oduck.api.domain.notifier.service;

import io.oduck.api.domain.contact.infra.event.ContactEvent;
import io.oduck.api.global.notification.Notifier;
import io.oduck.api.global.notification.dto.Message;
import io.oduck.api.global.webHook.DiscordWebhook.EmbedObject;
import java.awt.Color;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ContactEventHandler {
    private final Notifier notifier;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleContactEvent(ContactEvent contactEvent) {
        String title = contactEvent.getTitle();
        String content = contactEvent.getContent();
        String category = contactEvent.getType();
        String name = contactEvent.getName();

        String requestTime = new Date().toString();

        EmbedObject embed = new EmbedObject()
            .setTitle(title)
            .setDescription(String.format("[문의사항] %s 님의 문의사항", name))
            .setColor(new Color(000, 100, 255))
            .addField("CONTENT", content, false)
            .addField("CATEGORY", category, false)
            .addField("TIME", requestTime, false);

        notifier.sendNotification(Message.from(embed));
    }
}
