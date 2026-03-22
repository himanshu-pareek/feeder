package dev.javarush.feeder.content;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.javarush.feeder.content.memory.InMemoryUserFeedEntryRepository;
import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedEntry;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserFeedEntryServiceTest {

    private UserFeedEntryRepository userFeedEntryRepository;
    private UserFeedEntryService userFeedEntryService;

    @BeforeEach
    void setUp() {
        userFeedEntryRepository = new InMemoryUserFeedEntryRepository();
        userFeedEntryService = new UserFeedEntryService(userFeedEntryRepository);
    }

    @Test
    void testCreateEntriesForUserWithDuplicateKeyOverwrites() {
        // Arrange
        String userId = "user-1";
        URI feedUri = URI.create("https://example.com/rss");
        FeedEntry entry = FeedEntry.builder().uri("entry-1").title("Original Title").build();
        Feed mockFeed = new Feed(feedUri, "Title", "Link", "Desc");
        mockFeed.setEntries(List.of(entry));

        // Act - First creation
        userFeedEntryService.createEntriesForUser(userId, mockFeed);

        // Modify feed entry and create again
        FeedEntry modifiedEntry = FeedEntry.builder().uri("entry-1").title("Modified Title").build();
        Feed modifiedMockFeed = new Feed(feedUri, "Title", "Link", "Desc");
        modifiedMockFeed.setEntries(List.of(modifiedEntry));
        
        userFeedEntryService.createEntriesForUser(userId, modifiedMockFeed);

        // Assert
        Collection<UserFeedEntry> userEntries = userFeedEntryRepository.findByUserId(userId);
        
        // Should still be only 1 entry because they have the same identity
        assertEquals(1, userEntries.size());
        
        // The title should be from the latest save (overwritten)
        assertEquals("Modified Title", userEntries.iterator().next().getTitle());
    }

    @Test
    void testCreateMultipleEntriesForSameUser() {
        // Arrange
        String userId = "user-1";
        URI feedUri = URI.create("https://example.com/rss");
        Feed mockFeed = new Feed(feedUri, "Title", "Link", "Desc");
        mockFeed.setEntries(List.of(
            FeedEntry.builder().uri("entry-1").build(),
            FeedEntry.builder().uri("entry-2").build()
        ));

        // Act
        userFeedEntryService.createEntriesForUser(userId, mockFeed);

        // Assert
        Collection<UserFeedEntry> userEntries = userFeedEntryRepository.findByUserId(userId);
        assertEquals(2, userEntries.size());
    }
}
