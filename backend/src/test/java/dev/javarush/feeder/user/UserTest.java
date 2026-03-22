package dev.javarush.feeder.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.subscription.Subscription;
import java.net.URI;
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
        Feed feed = new Feed(feedUri, "Title", "Link", "Desc");

        boolean subscribed = user.subscribeTo(feed);

        assertTrue(subscribed);
        assertTrue(user.isSubscribedTo(feed));
        assertEquals(1, user.getSubscriptions().size());
        
        Subscription subscription = user.getSubscriptions().iterator().next();
        assertEquals(feedUri, subscription.feedUri());
    }

    @Test
    void testSubscribeToDuplicateFeedReturnsFalse() {
        User user = new User("user-1");
        URI feedUri = URI.create("https://example.com/feed");
        Feed feed = new Feed(feedUri, "Title", "Link", "Desc");

        user.subscribeTo(feed);
        boolean secondAttempt = user.subscribeTo(feed);

        assertFalse(secondAttempt);
        assertEquals(1, user.getSubscriptions().size());
    }

    @Test
    void testUnsubscribeFromFeed() {
        User user = new User("user-1");
        URI feedUri = URI.create("https://example.com/feed");
        Feed feed = new Feed(feedUri, "Title", "Link", "Desc");
        user.subscribeTo(feed);

        boolean unsubscribed = user.unsubscribeFrom(feedUri);

        assertTrue(unsubscribed);
        assertFalse(user.isSubscribedTo(feed));
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
