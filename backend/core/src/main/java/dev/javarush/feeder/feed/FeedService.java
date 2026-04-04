package dev.javarush.feeder.feed;

import dev.javarush.feeder.feed.exception.FeedFetchException;
import dev.javarush.feeder.feed.exception.FeedNotFoundException;
import dev.javarush.feeder.util.DateTimeUtil;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedService {

  private static Logger logger = LoggerFactory.getLogger(FeedService.class);

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
      logger.info("Syncing feed {}", feed.getUri());
      fetchAndSave(feed.getUri());
  }

  public void syncFeeds(Consumer<Feed> afterSyncingFeed) {
    LocalDateTime now = DateTimeUtil.nowInUTC();
    this.feedRepository.findAll()
        .stream()
        .filter(feed -> feed.getLastSyncedAt().isBefore(now))
        .forEach(feed -> {
          syncFeed(feed);
          afterSyncingFeed.accept(feed);
        });
  }
}
