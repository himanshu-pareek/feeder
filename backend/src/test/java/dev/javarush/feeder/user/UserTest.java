package dev.javarush.feeder.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.javarush.feeder.subscription.Subscription;
import java.net.URI;
import java.util.Collection;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User("user-1");
        assertEquals("user-1", user.getId());
        assertTrue(user.getSubscriptions().isEmpty());
    }

    @Test
    void testSubscribeToFeed() {
        User user = new User("user-1");
        URI feedUri = URI.create("https://example.com/feed");

        boolean subscribed = user.subscribeTo(feedUri);

        assertTrue(subscribed);
        assertTrue(user.isSubscribedTo(feedUri));
        assertEquals(1, user.getSubscriptions().size());
        
        Subscription subscription = user.getSubscriptions().iterator().next();
        assertEquals(feedUri, subscription.feedUri());
    }

    @Test
    void testSubscribeToDuplicateFeedReturnsFalse() {
        User user = new User("user-1");
        URI feedUri = URI.create("https://example.com/feed");

        user.subscribeTo(feedUri);
        boolean secondAttempt = user.subscribeTo(feedUri);

        assertFalse(secondAttempt);
        assertEquals(1, user.getSubscriptions().size());
    }

    @Test
    void testUnsubscribeFromFeed() {
        User user = new User("user-1");
        URI feedUri = URI.create("https://example.com/feed");
        user.subscribeTo(feedUri);

        boolean unsubscribed = user.unsubscribeFrom(feedUri);

        assertTrue(unsubscribed);
        assertFalse(user.isSubscribedTo(feedUri));
        assertTrue(user.getSubscriptions().isEmpty());
    }

    @Test
    void testUnsubscribeFromNonExistentFeedReturnsFalse() {
        User user = new User("user-1");
        URI feedUri = URI.create("https://example.com/feed");

        boolean unsubscribed = user.unsubscribeFrom(feedUri);

        assertFalse(unsubscribed);
    }

    @Test
    void testEqualsAndHashCode() {
        User user1 = new User("user-1");
        User user2 = new User("user-1");
        User user3 = new User("user-2");

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
        assertFalse(user1.equals(user3));
    }
}
