package dev.javarush.feeder.memory.feed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedFetcher;
import dev.javarush.feeder.feed.FeedRepository;
import dev.javarush.feeder.feed.FeedService;
import dev.javarush.feeder.feed.exception.FeedFetchException;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FeedServiceTest {

    private FeedRepository feedRepository;
    private FeedService feedService;
    private StubFeedFetcher feedFetcher;

    @BeforeEach
    void setUp() {
        feedRepository = new InMemoryFeedRepository();
        feedFetcher = new StubFeedFetcher();
        feedService = new FeedService(feedRepository, feedFetcher);
    }

    @Test
    void testGetOrCreateFeedWhenNotExists() {
        // Arrange
        URI uri = URI.create("https://example.com/rss");
        Feed mockFeed = new Feed(uri, "Title", "Link", "Desc");
        feedFetcher.setNextFeed(mockFeed);

        // Act
        Feed result = feedService.getOrCreateFeed(uri);

        // Assert
        assertNotNull(result);
        assertEquals(uri, result.getUri());
        assertTrue(feedRepository.findByUri(uri).isPresent());
    }

    @Test
    void testGetOrCreateFeedWhenExists() {
        // Arrange
        URI uri = URI.create("https://example.com/rss");
        Feed existingFeed = new Feed(uri, "Existing", "Link", "Desc");
        feedRepository.save(existingFeed);

        // Act
        Feed result = feedService.getOrCreateFeed(uri);

        // Assert
        assertEquals("Existing", result.getTitle());
    }

    @Test
    void testGetOrCreateFeedThrowsOnFetchError() {
        URI uri = URI.create("https://example.com/error");
        
        assertThrows(FeedFetchException.class, () -> feedService.getOrCreateFeed(uri));
    }

    private static class StubFeedFetcher implements FeedFetcher {
        private Feed nextFeed;

        public void setNextFeed(Feed nextFeed) {
            this.nextFeed = nextFeed;
        }

        @Override
        public Feed fetchByUri(URI uri) {
            if (nextFeed != null) return nextFeed;
            throw new FeedFetchException("Fetch failed", null);
        }
    }
}
