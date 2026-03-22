package dev.javarush.feeder.user.use_case;

import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.user.User;
import dev.javarush.feeder.user.UserService;
import java.net.URI;
import java.util.Objects;

public class SubscribeToAFeed {
  private final UserService userService;
  private final FeedService feedService;

  public SubscribeToAFeed(UserService userService, FeedService feedService) {
    this.userService = Objects.requireNonNull(userService);
    this.feedService = Objects.requireNonNull(feedService);
  }

  public void execute(String userId, URI feedUri) {
    User user = userService.getUser(userId);
    Feed feed = feedService.getOrCreateFeed(feedUri);
    this.userService.subscribe(user, feed);
  }
}
