package dev.javarush.feeder.api.event;

import dev.javarush.feeder.event.EventPublisher;
import dev.javarush.feeder.event.FeedSubscribe;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class UserFeedSubscriptionEventPublisher
    implements EventPublisher<FeedSubscribe>, ApplicationEventPublisherAware {
  private ApplicationEventPublisher eventPublisher;

  @Override
  public void publish(FeedSubscribe event) {
    this.eventPublisher.publishEvent(event);
  }

  @Override
  public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }
}
