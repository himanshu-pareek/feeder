package dev.javarush.feeder.feed;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

public interface FeedFetcher {
  Feed fetchByUri(URI uri);
}
