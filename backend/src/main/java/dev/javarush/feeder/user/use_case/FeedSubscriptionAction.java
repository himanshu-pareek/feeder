package dev.javarush.feeder.user.use_case;

import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.user.User;
import dev.javarush.feeder.user.UserService;
import dev.javarush.feeder.user.events.UserSubscribedEvent;
import java.net.URI;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

public class FeedSubscriptionAction implements ApplicationEventPublisherAware {
  private final UserService userService;
  private final FeedService feedService;
  private ApplicationEventPublisher eventPublisher;

  public FeedSubscriptionAction(UserService userService, FeedService feedService) {
    this.userService = Objects.requireNonNull(userService);
    this.feedService = Objects.requireNonNull(feedService);
  }

  public void execute(String userId, URI feedUri) {
    User user = userService.getUser(userId);
    Feed feed = feedService.getOrCreateFeed(feedUri);
    this.userService.subscribe(user, feed);
    this.eventPublisher.publishEvent(new UserSubscribedEvent(userId, feedUri));
  }

  @Override
  public void setApplicationEventPublisher(@NonNull ApplicationEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }
}
