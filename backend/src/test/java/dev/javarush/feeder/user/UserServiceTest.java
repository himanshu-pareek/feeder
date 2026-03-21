package dev.javarush.feeder.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.javarush.feeder.content.InMemoryUserFeedEntryRepository;
import dev.javarush.feeder.content.UserFeedEntry;
import dev.javarush.feeder.content.UserFeedEntryRepository;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedEntry;
import dev.javarush.feeder.feed.FeedFetcher;
import dev.javarush.feeder.feed.FeedRepository;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.feed.InMemoryFeedRepository;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private UserRepository userRepository;
    private FeedRepository feedRepository;
    private UserFeedEntryRepository userFeedEntryRepository;
    private UserService userService;
    private FeedService feedService;
    private StubFeedFetcher feedFetcher;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        feedRepository = new InMemoryFeedRepository();
        userFeedEntryRepository = new InMemoryUserFeedEntryRepository();
        feedFetcher = new StubFeedFetcher();
        feedService = new FeedService(feedRepository, feedFetcher);
        userService = new UserService(userRepository, feedService, userFeedEntryRepository);
    }

    @Test
    void testSubscribeToNewFeed() {
        // Arrange
        String userId = "user-1";
        userRepository.save(new User(userId));
        URI feedUri = URI.create("https://example.com/rss");
        Feed mockFeed = new Feed(feedUri, "Title", "Link", "Desc");
        mockFeed.setEntries(List.of(
            FeedEntry.builder().uri("entry-1").build(),
            FeedEntry.builder().uri("entry-2").build()
        ));
        feedFetcher.setNextFeed(mockFeed);

        // Act
        userService.subscribe(userId, feedUri);

        // Assert
        User user = userRepository.findById(userId).get();
        assertTrue(user.isSubscribedTo(feedUri));
        
        assertTrue(feedRepository.findByUri(feedUri).isPresent());
        
        Collection<UserFeedEntry> userEntries = userFeedEntryRepository.findByUserId(userId);
        assertEquals(2, userEntries.size());
    }

    @Test
    void testSubscribeThrowsExceptionIfUserNotFound() {
        assertThrows(UserNotFoundException.class, () -> 
            userService.subscribe("non-existent", URI.create("https://example.com"))
        );
    }

    @Test
    void testSubscribeThrowsExceptionIfAlreadySubscribed() {
        String userId = "user-1";
        URI feedUri = URI.create("https://example.com/rss");
        User user = new User(userId);
        user.subscribeTo(feedUri);
        userRepository.save(user);

        assertThrows(AlreadySubscribedException.class, () -> 
            userService.subscribe(userId, feedUri)
        );
    }

    @Test
    void testCreateEntriesForUser() {
        // Arrange
        String userId = "user-1";
        URI feedUri = URI.create("https://example.com/rss");
        Feed mockFeed = new Feed(feedUri, "Title", "Link", "Desc");
        mockFeed.setEntries(List.of(
            FeedEntry.builder().uri("entry-1").build()
        ));

        // Act
        userService.createEntriesForUser(userId, mockFeed);

        // Assert
        Collection<UserFeedEntry> userEntries = userFeedEntryRepository.findByUserId(userId);
        assertEquals(1, userEntries.size());
    }

    // A simple stub for FeedFetcher instead of a complex mocking framework
    private static class StubFeedFetcher implements FeedFetcher {
        private Feed nextFeed;

        public void setNextFeed(Feed nextFeed) {
            this.nextFeed = nextFeed;
        }

        @Override
        public Feed fetchByUri(URI uri) {
            if (nextFeed != null) return nextFeed;
            throw new RuntimeException("No feed configured in stub");
        }
    }
}
