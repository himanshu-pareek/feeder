package dev.javarush.feeder.user;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.eventbus.EventBus;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedFetcher;
import dev.javarush.feeder.feed.FeedRepository;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.feed.InMemoryFeedRepository;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private UserRepository userRepository;
    private FeedRepository feedRepository;
    private UserService userService;
    private FeedService feedService;
    private StubFeedFetcher feedFetcher;
    private EventBus eventBus;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        feedRepository = new InMemoryFeedRepository();
        feedFetcher = new StubFeedFetcher();
        feedService = new FeedService(feedRepository, feedFetcher);
        eventBus = new EventBus();
        userService = new UserService(userRepository, feedService, eventBus);
    }

    @Test
    void testSubscribeToNewFeed() {
        // Arrange
        String userId = "user-1";
        userRepository.save(new User(userId));
        URI feedUri = URI.create("https://example.com/rss");
        Feed mockFeed = new Feed(feedUri, "Title", "Link", "Desc");
        feedFetcher.setNextFeed(mockFeed);

        // Act
        userService.subscribe(userId, feedUri);

        // Assert
        User user = userRepository.findById(userId).get();
        assertTrue(user.isSubscribedTo(feedUri));
        assertTrue(feedRepository.findByUri(feedUri).isPresent());
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
