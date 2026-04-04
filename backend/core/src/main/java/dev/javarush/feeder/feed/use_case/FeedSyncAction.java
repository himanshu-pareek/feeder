package dev.javarush.feeder.feed.use_case;

import dev.javarush.feeder.event.EventPublisher;
import dev.javarush.feeder.event.FeedSync;
import dev.javarush.feeder.feed.FeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedSyncAction {

  private static Logger logger = LoggerFactory.getLogger(FeedSyncAction.class);

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
    logger.info("Syncing all feeds");
    this.feedService.syncFeeds(feed -> {
      feedSyncEventPublisher.publish(new FeedSync(feed.getUri()));
    });
  }
}
