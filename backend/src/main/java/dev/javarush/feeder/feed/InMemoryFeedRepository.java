package dev.javarush.feeder.feed;

import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryFeedRepository implements FeedRepository{
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
}
