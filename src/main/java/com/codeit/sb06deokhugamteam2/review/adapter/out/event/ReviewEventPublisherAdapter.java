package com.codeit.sb06deokhugamteam2.review.adapter.out.event;

import com.codeit.sb06deokhugamteam2.review.application.port.out.ReviewEventPublisherPort;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class ReviewEventPublisherAdapter implements ReviewEventPublisherPort {

    private final ApplicationEventPublisher publisher;

    public ReviewEventPublisherAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(Object event) {
        publisher.publishEvent(event);
    }
}
