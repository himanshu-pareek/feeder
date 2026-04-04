package dev.javarush.feeder.content.use_case;

import dev.javarush.feeder.content.UserFeedEntryService;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.user.UserService;
import java.net.URI;

public class UserFeedEntriesSyncAction {
  private final UserService userService;
  private final UserFeedEntryService userFeedEntryService;
  private final FeedService feedService;

  public UserFeedEntriesSyncAction(UserService userService, UserFeedEntryService userFeedEntryService,
                                   FeedService feedService) {
    this.userService = userService;
    this.userFeedEntryService = userFeedEntryService;
    this.feedService = feedService;
  }

  public void execute(URI feedUri) {
    Feed feed = feedService.getFeed(feedUri);
    userService.getSubscribersFor(feed.getUri())
        .forEach(userId -> {
          userFeedEntryService.createEntriesForUser(userId, feed);
        });
  }
}
