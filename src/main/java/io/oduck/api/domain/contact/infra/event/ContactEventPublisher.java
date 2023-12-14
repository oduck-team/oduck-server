package io.oduck.api.domain.contact.infra.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ContactEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void contact(ContactEvent contactEvent) {
        eventPublisher.publishEvent(contactEvent);
    }
}
