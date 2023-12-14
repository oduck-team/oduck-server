package io.oduck.api.global.notification;

import io.oduck.api.global.notification.dto.Message;

public interface Notifier {
    void sendNotification(Message message);
}