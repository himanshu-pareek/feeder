package dev.javarush.feeder.memory.feed;

import dev.javarush.feeder.feed.Feed;
import dev.javarush.feeder.feed.FeedRepository;
import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFeedRepository implements FeedRepository {
  private final Map<URI, Feed> feeds;

  public InMemoryFeedRepository() {
    this.feeds = new ConcurrentHashMap<>();
  }

  @Override
  public void save(Feed feed) {
    this.feeds.put(feed.getUri(), new Feed(feed));
  }

  @Override
  public Optional<Feed> findByUri(URI uri) {
    return Optional.ofNullable(this.feeds.get(uri)).map(Feed::new);
  }

  @Override
  public Collection<Feed> findAll() {
    return feeds.values();
  }
}
