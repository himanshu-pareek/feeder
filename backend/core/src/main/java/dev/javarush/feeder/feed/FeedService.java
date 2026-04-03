package dev.javarush.feeder.feed;

import dev.javarush.feeder.feed.exception.FeedFetchException;
import dev.javarush.feeder.feed.exception.FeedNotFoundException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedFetcher feedFetcher;

    public FeedService(FeedRepository feedRepository, FeedFetcher feedFetcher) {
        this.feedRepository = Objects.requireNonNull(feedRepository);
        this.feedFetcher = Objects.requireNonNull(feedFetcher);
    }

    /**
     * Retrieves a feed from the repository by its URI.
     * If not found, fetches it using the FeedFetcher, saves it, and returns it.
     */
    public Feed getOrCreateFeed(URI feedUri) {
        return feedRepository.findByUri(feedUri)
            .orElseGet(() -> {
                try {
                  return fetchAndSave(feedUri);
                } catch (Exception e) {
                    throw new FeedFetchException("Failed to fetch feed: " + feedUri, e);
                }
            });
    }

  private Feed fetchAndSave(URI feedUri) {
    Feed fetchedFeed = feedFetcher.fetchByUri(feedUri);
    feedRepository.save(fetchedFeed);
    return fetchedFeed;
  }

  public Feed getFeed(URI uri) {
    return feedRepository.findByUri(uri)
        .orElseThrow(() -> new FeedNotFoundException("Feed not found: " + uri));
  }

  private void syncFeed(Feed feed) {
      fetchAndSave(feed.getUri());
  }

  public void syncFeeds(Consumer<Feed> afterSyncingFeed) {
    LocalDateTime now = LocalDateTime.now(ZoneId.of("utc"));
    this.feedRepository.findAll()
        .stream()
        .filter(feed -> feed.getLastSyncedAt().isBefore(now))
        .forEach(feed -> {
          syncFeed(feed);
          afterSyncingFeed.accept(feed);
        });
  }
}
