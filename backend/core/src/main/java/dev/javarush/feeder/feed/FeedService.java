package dev.javarush.feeder.feed;

import dev.javarush.feeder.feed.exception.FeedFetchException;
import dev.javarush.feeder.feed.exception.FeedNotFoundException;
import java.net.URI;
import java.util.Objects;

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
                    Feed fetchedFeed = feedFetcher.fetchByUri(feedUri);
                    feedRepository.save(fetchedFeed);
                    return fetchedFeed;
                } catch (Exception e) {
                    throw new FeedFetchException("Failed to fetch feed: " + feedUri, e);
                }
            });
    }

  public Feed getFeed(URI uri) {
    return feedRepository.findByUri(uri)
        .orElseThrow(() -> new FeedNotFoundException("Feed not found: " + uri));
  }
}
