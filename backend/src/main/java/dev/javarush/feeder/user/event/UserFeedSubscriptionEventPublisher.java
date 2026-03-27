package dev.javarush.feeder.user.event;

import dev.javarush.feeder.event.EventPublisher;
import dev.javarush.feeder.event.FeedSubscriptionEvent;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class UserFeedSubscriptionEventPublisher
    implements EventPublisher<FeedSubscriptionEvent>, ApplicationEventPublisherAware {
  private ApplicationEventPublisher eventPublisher;

  @Override
  public void publish(FeedSubscriptionEvent event) {
    this.eventPublisher.publishEvent(event);
  }

  @Override
  public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }
}
