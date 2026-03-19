package dev.javarush.feeder.feed;

import java.net.URI;
import java.util.Optional;

public interface FeedRepository {
    void save(Feed feed);

    Optional<Feed> findByUri(URI uri);
}
