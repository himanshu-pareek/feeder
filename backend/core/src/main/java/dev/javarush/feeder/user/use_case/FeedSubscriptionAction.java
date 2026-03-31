package dev.javarush.feeder.user.use_case;

import dev.javarush.feeder.event.EventPublisher;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.user.User;
import dev.javarush.feeder.user.UserService;
import dev.javarush.feeder.event.FeedSubscribe;
import java.net.URI;
import java.util.Objects;

public class FeedSubscriptionAction{
  private final UserService userService;
  private final FeedService feedService;
  private final EventPublisher<FeedSubscribe> eventPublisher;

  public FeedSubscriptionAction(UserService userService, FeedService feedService,
                                EventPublisher<FeedSubscribe> eventPublisher) {
    this.userService = Objects.requireNonNull(userService);
    this.feedService = Objects.requireNonNull(feedService);
    this.eventPublisher = Objects.requireNonNull(eventPublisher);
  }

  public void execute(String userId, URI feedUri) {
    User user = userService.getUser(userId);
    Feed feed = feedService.getOrCreateFeed(feedUri);
    this.userService.subscribe(user, feed);
    this.eventPublisher.publish(new FeedSubscribe(userId, feedUri));
  }
}
