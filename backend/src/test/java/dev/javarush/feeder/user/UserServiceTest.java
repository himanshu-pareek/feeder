package dev.javarush.feeder.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.eventbus.EventBus;
import dev.javarush.feeder.feed.Feed;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;
    private EventBus eventBus;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        eventBus = new EventBus();
        userService = new UserService(userRepository, eventBus);
    }

    @Test
    void testSubscribeToFeed() {
        // Arrange
        User user = new User("user-1");
        userRepository.save(user);
        URI feedUri = URI.create("https://example.com/rss");
        Feed feed = new Feed(feedUri, "Title", "Link", "Desc");

        // Act
        userService.subscribe(user, feed);

        // Assert
        assertTrue(user.isSubscribedTo(feed));
        assertTrue(userRepository.findById("user-1").isPresent());
    }

    @Test
    void testGetUser() {
        // Arrange
        User user = new User("user-1");
        userRepository.save(user);

        // Act
        User foundUser = userService.getUser("user-1");

        // Assert
        assertEquals(user, foundUser);
    }

    @Test
    void testGetUserThrowsExceptionIfNotFound() {
        assertThrows(UserNotFoundException.class, () -> 
            userService.getUser("non-existent")
        );
    }

    @Test
    void testSubscribeThrowsExceptionIfAlreadySubscribed() {
        // Arrange
        User user = new User("user-1");
        URI feedUri = URI.create("https://example.com/rss");
        Feed feed = new Feed(feedUri, "Title", "Link", "Desc");
        user.subscribeTo(feed);
        userRepository.save(user);

        // Act & Assert
        assertThrows(AlreadySubscribedException.class, () -> 
            userService.subscribe(user, feed)
        );
    }
}
