package dev.javarush.feeder.feed;

import java.net.URI;

public interface FeedFetcher {
  Feed fetchByUri(URI uri);
}
