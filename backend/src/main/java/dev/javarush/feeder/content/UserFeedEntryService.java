package dev.javarush.feeder.content;

import dev.javarush.feeder.feed.Feed;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service for managing UserFeedEntry business logic.
 */
public class UserFeedEntryService {

    private final UserFeedEntryRepository userFeedEntryRepository;

    public UserFeedEntryService(UserFeedEntryRepository userFeedEntryRepository) {
        this.userFeedEntryRepository = Objects.requireNonNull(userFeedEntryRepository);
    }

    /**
     * Creates user-specific feed entries for all items in the provided feed.
     */
    public void createEntriesForUser(String userId, Feed feed) {
        if (feed.getEntries() != null) {
            List<UserFeedEntry> userEntries = feed.getEntries().stream()
                .map(entry -> new UserFeedEntry(userId, feed.getUri(), entry))
                .collect(Collectors.toList());
            userFeedEntryRepository.saveAll(userEntries);
        }
    }
}
