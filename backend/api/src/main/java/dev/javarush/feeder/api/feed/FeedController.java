package dev.javarush.feeder.api.feed;

import dev.javarush.feeder.feed.use_case.FeedSyncAction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("feeds")
public class FeedController {
  private final FeedSyncAction feedSyncAction;

  public FeedController(FeedSyncAction feedSyncAction) {
    this.feedSyncAction = feedSyncAction;
  }

  @PostMapping("sync")
  void syncFeeds() {
    this.feedSyncAction.syncFeeds();
  }
}
