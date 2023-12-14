package io.oduck.api.global.advice.infra;

import io.oduck.api.global.notification.Notifier;
import io.oduck.api.global.notification.dto.Message;
import io.oduck.api.global.webHook.DiscordWebhook;
import io.oduck.api.global.webHook.DiscordWebhook.EmbedObject;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Qualifier("exceptionNotifier")
@RequiredArgsConstructor
@Slf4j
public class ExceptionNotifier implements Notifier {

    @Value("${config.webhook.url}")
    private String url;

    @Override
    public void sendNotification(Message message) {
        DiscordWebhook webhook = new DiscordWebhook(url);

        EmbedObject content = message.getContent();
        webhook.addEmbed(content);

        try{
            webhook.execute();
        } catch (IOException exception) {
            log.error("Discord WebHook Error");
        }
    }
}
