package dev.javarush.feeder.content.use_case;

import dev.javarush.feeder.content.UserFeedEntry;
import dev.javarush.feeder.content.UserFeedEntryService;
import java.util.Collection;

public class FeedEntriesGetAction {
  private final UserFeedEntryService userFeedEntryService;

  public FeedEntriesGetAction(UserFeedEntryService userFeedEntryService) {
    this.userFeedEntryService = userFeedEntryService;
  }

  public Collection<UserFeedEntry> execute(String userId) {
    return this.userFeedEntryService.getEntriesForUser(userId);
  }
}
