package dev.javarush.feeder.api.event;

import dev.javarush.feeder.content.use_case.UserFeedEntriesSyncAction;
import dev.javarush.feeder.event.FeedSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class FeedSyncEventListener {
  private static Logger logger = LoggerFactory.getLogger(FeedSyncEventListener.class);

  private final UserFeedEntriesSyncAction userFeedEntriesSyncAction;

  public FeedSyncEventListener(UserFeedEntriesSyncAction userFeedEntriesSyncAction) {
    this.userFeedEntriesSyncAction = userFeedEntriesSyncAction;
  }

  @Async
  @EventListener(FeedSync.class)
  void handleFeedSync(FeedSync event) {
    logger.info("Handling event {}", event);
    this.userFeedEntriesSyncAction.execute(event.feedUri());
  }
}
