package dev.javarush.feeder.feed.use_case;

import dev.javarush.feeder.event.EventPublisher;
import dev.javarush.feeder.event.FeedSync;
import dev.javarush.feeder.feed.FeedService;

public class FeedSyncAction {
  private final FeedService feedService;
  private final EventPublisher<FeedSync> feedSyncEventPublisher;

  public FeedSyncAction(
      FeedService feedService,
      EventPublisher<FeedSync> feedSyncEventPublisher
  ) {
    this.feedService = feedService;
    this.feedSyncEventPublisher = feedSyncEventPublisher;
  }

  public void syncFeeds() {
    this.feedService.syncFeeds(feed -> {
      feedSyncEventPublisher.publish(new FeedSync(feed.getUri()));
    });
  }
}
