package dev.javarush.feeder.memory.feed;

import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedEntry;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFeedRepositoryTest {
    @Test
    void shouldCloneFeedOnSaveAndRetrieve() {
        InMemoryFeedRepository repository = new InMemoryFeedRepository();
        URI uri = URI.create("https://example.com/feed");
        Feed originalFeed = new Feed(uri, "Title", "Link", "Description");
        List<FeedEntry> entries = new ArrayList<>();
        entries.add(FeedEntry.builder().title("Entry 1").link("Link 1").build());
        originalFeed.setEntries(entries);

        repository.save(originalFeed);

        // Mutate original feed after save
        originalFeed.setUri(URI.create("https://example.com/feed1"));
        // FeedEntry is now a record (immutable), so we cannot mutate it directly.
        // We'll replace the entry in the list to simulate mutation of the feed's content.
        List<FeedEntry> mutatedEntries = new ArrayList<>();
        mutatedEntries.add(FeedEntry.builder().title("Mutated Entry Title").link("Link 1").build());
        originalFeed.setEntries(mutatedEntries);

        Optional<Feed> retrievedFeedOpt = repository.findByUri(uri);
        assertTrue(retrievedFeedOpt.isPresent());
        Feed retrievedFeed = retrievedFeedOpt.get();

        // Check that retrieved feed is not mutated
        assertEquals("Title", retrievedFeed.getTitle());
        assertEquals(URI.create("https://example.com/feed"), retrievedFeed.getUri());
        assertEquals("Entry 1", retrievedFeed.getEntries().getFirst().title());
        assertNotSame(originalFeed, retrievedFeed);

        retrievedFeed.setUri(URI.create("https://example.com/feed1"));
        
        // Retrieve again to check if it was mutated in the map
        var secondRetrieval = repository.findByUri(uri);
        assertTrue(secondRetrieval.isPresent());
        assertEquals("Title", secondRetrieval.get().getTitle());
        assertEquals(URI.create("https://example.com/feed"), secondRetrieval.get().getUri());
        assertNotSame(retrievedFeed, secondRetrieval.get());
    }
}
