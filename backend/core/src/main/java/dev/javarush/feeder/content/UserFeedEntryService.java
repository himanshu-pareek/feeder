package dev.javarush.feeder.content;

import dev.javarush.feeder.feed.Feed;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for managing UserFeedEntry business logic.
 */
public class UserFeedEntryService {
    private static Logger logger = LoggerFactory.getLogger(UserFeedEntryService.class);

    private final UserFeedEntryRepository userFeedEntryRepository;

    public UserFeedEntryService(UserFeedEntryRepository userFeedEntryRepository) {
        this.userFeedEntryRepository = Objects.requireNonNull(userFeedEntryRepository);
    }

    /**
     * Creates user-specific feed entries for all items in the provided feed.
     */
    public void createEntriesForUser(String userId, Feed feed) {
        logger.info("Creating entries for userId: {}, feedUri: {}", userId, feed.getUri());
        if (feed.getEntries() != null) {
            List<UserFeedEntry> userEntries = feed.getEntries().stream()
                .filter(entry -> {
                        var alreadyExists = this.userFeedEntryRepository.containsEntry(
                            userId,
                            feed.getUri(),
                            entry.uri()
                        );
                        if (alreadyExists) {
                            logger.info("Feed entry already exist for userId:{}, feedUri:{}, entryUri:{}", userId, feed.getUri(), entry.uri());
                        } else {
                            logger.info("Creating entry for userId:{}, feedUri:{}, entryUri:{}", userId, feed.getUri(), entry.uri());
                        }
                        return !alreadyExists;
                    }
                )
                .map(entry -> new UserFeedEntry(userId, feed.getUri(), entry))
                .collect(Collectors.toList());
            userFeedEntryRepository.saveAll(userEntries);
        }
    }

    /**
     * Retrieves all feed entries for the specified user.
     */
    public Collection<UserFeedEntry> getEntriesForUser(String userId) {
        return userFeedEntryRepository.findByUserId(userId);
    }
}
