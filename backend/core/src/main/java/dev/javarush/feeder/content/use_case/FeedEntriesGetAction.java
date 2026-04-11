package dev.javarush.feeder.content.use_case;

import dev.javarush.feeder.content.UserFeedEntries;
import dev.javarush.feeder.content.UserFeedEntry;
import dev.javarush.feeder.content.UserFeedEntryService;
import java.util.Collection;

public class FeedEntriesGetAction {
  private final UserFeedEntryService userFeedEntryService;

  public FeedEntriesGetAction(UserFeedEntryService userFeedEntryService) {
    this.userFeedEntryService = userFeedEntryService;
  }

  public UserFeedEntries execute(String userId) {
    var entries = this.userFeedEntryService.getEntriesForUser(userId);
    return new UserFeedEntries(
        1,
        entries.size(),
        entries
    );
  }
}
