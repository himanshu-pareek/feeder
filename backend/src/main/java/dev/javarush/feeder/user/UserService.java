package dev.javarush.feeder.user;

import dev.javarush.feeder.content.UserFeedEntry;
import dev.javarush.feeder.content.UserFeedEntryRepository;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedService;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserService {

    private final UserRepository userRepository;
    private final FeedService feedService;
    private final UserFeedEntryRepository userFeedEntryRepository;

    public UserService(UserRepository userRepository,
                       FeedService feedService,
                       UserFeedEntryRepository userFeedEntryRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
        this.feedService = Objects.requireNonNull(feedService);
        this.userFeedEntryRepository = Objects.requireNonNull(userFeedEntryRepository);
    }

    /**
     * Subscribes a user to a feed and creates UserFeedEntry for existing items.
     */
    public void subscribe(String userId, URI feedUri) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        if (user.isSubscribedTo(feedUri)) {
            throw new AlreadySubscribedException("User is already subscribed to feed: " + feedUri);
        }

        Feed feed = feedService.getOrCreateFeed(feedUri);

        if (user.subscribeTo(feedUri)) {
            userRepository.save(user);
            createEntriesForUser(userId, feed);
        }
    }

    /**
     * Creates user-specific content for each entry in the feed.
     * This logic can be reused during feed synchronization.
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
