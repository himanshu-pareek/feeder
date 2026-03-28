package dev.javarush.feeder.subscription;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Value Object representing a subscription to a feed.
 */
public record Subscription(URI feedUri, String feedName, LocalDateTime subscribedAt) {
    public Subscription {
        Objects.requireNonNull(feedUri, "feedUri must not be null");
        Objects.requireNonNull(feedName, "feedName must not be null");
        Objects.requireNonNull(subscribedAt, "subscribedAt must not be null");
        feedName = feedName.strip();
        if (feedName.isBlank()) {
            throw new IllegalArgumentException("feedName must not be blank");
        }
    }

    public Subscription(URI feedUri, String feedName) {
        this(feedUri, feedName, LocalDateTime.now());
    }
}
