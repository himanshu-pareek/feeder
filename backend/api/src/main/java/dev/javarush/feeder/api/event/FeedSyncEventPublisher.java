package dev.javarush.feeder.api.event;

import dev.javarush.feeder.event.EventPublisher;
import dev.javarush.feeder.event.FeedSync;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class FeedSyncEventPublisher implements EventPublisher<FeedSync>,
    ApplicationEventPublisherAware {

  private static Logger logger = LoggerFactory.getLogger(FeedSyncEventPublisher.class);

  private ApplicationEventPublisher publisher;

  @Override
  public void publish(FeedSync event) {
    logger.info("Publishing event {}", event);
    this.publisher.publishEvent(event);
  }

  @Override
  public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher applicationEventPublisher) {
    this.publisher = applicationEventPublisher;
  }
}
